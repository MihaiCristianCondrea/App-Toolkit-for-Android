/**
 * The 3D Pixel 9 Pro XL, rendered with three.js on a canvas.
 *
 * The model is not in this repository: it is a BlenderKit Royalty Free asset that may be used in a
 * product but not redistributed, so it lives in the private android-skills repository and
 * `npm run assets` copies it from a sibling checkout into ignored public/models/. See README.md.
 *
 *   const pixel = await createPixel3D(canvas);      // or { modelUrl, dracoPath }
 *   pixel.resize(canvas.clientWidth, canvas.clientHeight);
 *   pixel.attachScreen(screenElement);              // a live 430 x 957 element, e.g. a frameless ScreenHost
 *   pixel.pose(turn);                               // 0 shows the back, 1 the front
 *
 * WebGL cannot paint a live page into a texture, so a CSS3DRenderer layer, drawn with the same
 * camera, carries the real screen element on the display plane: it turns with the phone, hides when
 * the display faces away, and stays interactive. Put the canvas in a positioned container; the layer
 * is inserted right after it. Rendering is on demand, never on a loop.
 */
import {
  ACESFilmicToneMapping,
  Box3,
  Group,
  Mesh,
  MeshPhysicalMaterial,
  PerspectiveCamera,
  PMREMGenerator,
  Scene,
  SRGBColorSpace,
  Vector3,
  WebGLRenderer,
} from "three";
import { DRACOLoader } from "three/examples/jsm/loaders/DRACOLoader.js";
import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader.js";
import { RoomEnvironment } from "three/examples/jsm/environments/RoomEnvironment.js";
import { CSS3DObject, CSS3DRenderer } from "three/examples/jsm/renderers/CSS3DRenderer.js";

export const PIXEL_MODEL_URL = "/models/pixel-9-pro-xl.glb";
export const DRACO_DECODER_PATH = "/draco/";
const SCREEN_MATERIAL = "GP9XL_Screen";
const FIELD_OF_VIEW = 18;
/** The kit's phone window in CSS pixels. */
export const SCREEN_WIDTH = 430;
export const SCREEN_HEIGHT = 957;
/** CSS3D maps world units to CSS pixels, so the scene works in millimetres. */
const MM = 1000;
/** The glass spans the whole front; the lit display sits inside the bezel. Millimetres. */
const BEZEL_MM = 2.4;
const DISPLAY_CORNER_MM = 8;
const FRONT_CAMERA_TOP_MM = 4.6;
const FRONT_CAMERA_MM = 3.4;

/** Smoothstep, so a turn starts and lands softly. */
export const easeTurn = (t) => {
  const x = Math.min(1, Math.max(0, t));
  return x * x * (3 - 2 * x);
};

/** Releases every geometry, material and texture under `root` (and in `extra`), once each. */
function disposeModel(root, extra = []) {
  const released = new Set();
  const release = (resource) => {
    if (!resource || released.has(resource)) return;
    released.add(resource);
    resource.dispose();
  };
  const releaseMaterial = (material) => {
    for (const value of Object.values(material)) {
      if (value && value !== material && typeof value.dispose === "function") release(value);
    }
    release(material);
  };
  root.traverse((node) => {
    release(node.geometry);
    if (node.material) [node.material].flat().forEach(releaseMaterial);
  });
  extra.forEach(releaseMaterial);
}

export async function createPixel3D(canvas, { modelUrl = PIXEL_MODEL_URL, dracoPath = DRACO_DECODER_PATH } = {}) {
  const renderer = new WebGLRenderer({ canvas, alpha: true, antialias: true });
  renderer.setPixelRatio(Math.min(2, window.devicePixelRatio || 1));
  renderer.outputColorSpace = SRGBColorSpace;
  renderer.toneMapping = ACESFilmicToneMapping;

  const scene = new Scene();
  const pmrem = new PMREMGenerator(renderer);
  const environment = pmrem.fromScene(new RoomEnvironment(), 0.04).texture;
  scene.environment = environment;

  // The meshes are Draco-compressed: without the decoder, loading fails.
  const draco = new DRACOLoader().setDecoderPath(dracoPath);
  const gltf = await new GLTFLoader().setDRACOLoader(draco).loadAsync(modelUrl);
  draco.dispose();

  const phone = gltf.scene;
  const glass = new MeshPhysicalMaterial({ color: 0x07090d, roughness: 0.08, metalness: 0, clearcoat: 1 });
  let screenY = 0;
  // Swapped-out display materials leave the tree, so they are disposed separately.
  const replaced = new Set();
  phone.traverse((node) => {
    if (!(node instanceof Mesh)) return;
    const swap = (material) => {
      if (material.name !== SCREEN_MATERIAL) return material;
      replaced.add(material);
      return glass;
    };
    node.material = Array.isArray(node.material) ? node.material.map(swap) : swap(node.material);
    const materials = Array.isArray(node.material) ? node.material : [node.material];
    if (materials.includes(glass)) screenY = new Box3().setFromObject(node).getCenter(new Vector3()).y;
  });

  // It lies flat (thickness is y). Centre it, stand it up with the display towards the camera,
  // then turn it the right way up: standing it up leaves the camera end at the bottom.
  const bounds = new Box3().setFromObject(phone);
  const centre = bounds.getCenter(new Vector3());
  const size = bounds.getSize(new Vector3());
  phone.position.sub(centre);
  const upright = new Group();
  upright.add(phone);
  upright.rotation.x = screenY >= centre.y ? Math.PI / 2 : -Math.PI / 2;
  const rightWayUp = new Group();
  rightWayUp.add(upright);
  rightWayUp.rotation.z = Math.PI;
  const turntable = new Group();
  turntable.add(rightWayUp);
  turntable.scale.setScalar(MM);
  scene.add(turntable);

  // Where the display is, in millimetres, measured at rest.
  scene.updateMatrixWorld(true);
  const display = new Box3();
  phone.traverse((node) => {
    if (!(node instanceof Mesh)) return;
    const materials = Array.isArray(node.material) ? node.material : [node.material];
    if (materials.includes(glass)) display.expandByObject(node);
  });
  const displayCentre = display.getCenter(new Vector3());
  const displaySize = display.getSize(new Vector3());

  const camera = new PerspectiveCamera(FIELD_OF_VIEW, 1, 1, 10000);
  camera.position.set(0, 0, (Math.max(size.x, size.z) * MM * 0.5) / Math.tan((FIELD_OF_VIEW * Math.PI) / 360));

  const cssScene = new Scene();
  const cssTurntable = new Group();
  cssScene.add(cssTurntable);
  const cssRenderer = new CSS3DRenderer();
  Object.assign(cssRenderer.domElement.style, { position: "absolute", inset: "0" });
  canvas.after(cssRenderer.domElement);
  let screenObject;

  const draw = () => {
    cssTurntable.rotation.copy(turntable.rotation);
    renderer.render(scene, camera);
    cssRenderer.render(cssScene, camera);
  };

  return {
    /** `turn` 0 shows the back, 1 the front; `tilt` leans it in radians. */
    pose(turn, tilt = 0) {
      turntable.rotation.y = Math.PI * (1 - easeTurn(turn));
      turntable.rotation.x = tilt;
      draw();
    },
    /** Places a live SCREEN_WIDTH x SCREEN_HEIGHT element on the display, inside the bezel. */
    attachScreen(screen) {
      if (screenObject) cssTurntable.remove(screenObject);
      const widthMm = displaySize.x - 2 * BEZEL_MM;
      const heightMm = displaySize.y - 2 * BEZEL_MM;
      const pxPerMm = SCREEN_WIDTH / widthMm;
      Object.assign(screen.style, {
        width: `${SCREEN_WIDTH}px`,
        height: `${SCREEN_HEIGHT}px`,
        overflow: "hidden",
        borderRadius: `${DISPLAY_CORNER_MM * pxPerMm}px`,
        backfaceVisibility: "hidden",
      });
      // The layer covers the model's front camera, so draw it on the screen.
      const lens = document.createElement("span");
      Object.assign(lens.style, {
        position: "absolute", left: "50%", transform: "translateX(-50%)", borderRadius: "50%", background: "#000",
        top: `${FRONT_CAMERA_TOP_MM * pxPerMm}px`, width: `${FRONT_CAMERA_MM * pxPerMm}px`, height: `${FRONT_CAMERA_MM * pxPerMm}px`,
      });
      screen.append(lens);
      screenObject = new CSS3DObject(screen);
      screenObject.position.set(displayCentre.x, displayCentre.y, display.max.z + 0.05);
      screenObject.scale.set(widthMm / SCREEN_WIDTH, heightMm / SCREEN_HEIGHT, 1);
      cssTurntable.add(screenObject);
      draw();
    },
    resize(width, height) {
      if (width <= 0 || height <= 0) return;
      renderer.setSize(width, height, false);
      cssRenderer.setSize(width, height);
      camera.aspect = width / height;
      camera.updateProjectionMatrix();
      draw();
    },
    dispose() {
      disposeModel(phone, [...replaced]);
      environment.dispose();
      pmrem.dispose();
      renderer.dispose();
      cssRenderer.domElement.remove();
    },
  };
}

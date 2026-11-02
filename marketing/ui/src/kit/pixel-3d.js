/**
 * The 3D Pixel 9 Pro XL, rendered with three.js on a canvas.
 *
 * The model is not in this repository: it is a BlenderKit Royalty Free asset that may be used in a
 * product but not redistributed, so it lives in the private android-skills repository and
 * `npm run assets` copies it from a sibling checkout into ignored public/models/. See README.md.
 *
 *   const pixel = await createPixel3D(canvas);      // or { modelUrl, dracoPath }
 *   pixel.resize(canvas.clientWidth, canvas.clientHeight);
 *   pixel.pose(turn);                               // 0 shows the back, 1 the front
 *
 * Lay the canvas exactly over Material Next's Pixel 9 Pro XL frame (the aspects match), turn the
 * phone to the front, then show the live screen in its place: the display material is dark glass,
 * so nothing fake is ever on the phone's screen. Rendering is on demand, never on a loop.
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

export const PIXEL_MODEL_URL = "/models/pixel-9-pro-xl.glb";
export const DRACO_DECODER_PATH = "/draco/";
const SCREEN_MATERIAL = "GP9XL_Screen";
const FIELD_OF_VIEW = 18;

/** Smoothstep, so a turn starts and lands softly. */
export const easeTurn = (t) => {
  const x = Math.min(1, Math.max(0, t));
  return x * x * (3 - 2 * x);
};

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
  phone.traverse((node) => {
    if (!(node instanceof Mesh)) return;
    const swap = (material) => (material.name === SCREEN_MATERIAL ? glass : material);
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
  scene.add(turntable);

  const camera = new PerspectiveCamera(FIELD_OF_VIEW, 1, 0.01, 10);
  camera.position.set(0, 0, (Math.max(size.x, size.z) * 0.5) / Math.tan((FIELD_OF_VIEW * Math.PI) / 360));
  const draw = () => renderer.render(scene, camera);

  return {
    /** `turn` 0 shows the back, 1 the front; `tilt` leans it in radians. */
    pose(turn, tilt = 0) {
      turntable.rotation.y = Math.PI * (1 - easeTurn(turn));
      turntable.rotation.x = tilt;
      draw();
    },
    resize(width, height) {
      if (width <= 0 || height <= 0) return;
      renderer.setSize(width, height, false);
      camera.aspect = width / height;
      camera.updateProjectionMatrix();
      draw();
    },
    dispose() {
      environment.dispose();
      pmrem.dispose();
      glass.dispose();
      renderer.dispose();
    },
  };
}

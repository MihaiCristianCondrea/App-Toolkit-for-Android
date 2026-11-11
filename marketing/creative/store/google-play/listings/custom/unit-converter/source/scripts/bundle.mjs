import {bundle as remotionBundle} from "@remotion/bundler";

/** Inline the two large local symbol fonts, avoiding interrupted HTTP font streams in still captures. */
export const inlineFonts=config=>({
 ...config,module:{...config.module,rules:config.module.rules.map(rule=>
  rule.test instanceof RegExp&&rule.test.test(".woff2")?{...rule,type:"asset/inline"}:rule
 )}
});
export const bundle=options=>remotionBundle({...options,webpackOverride:inlineFonts});

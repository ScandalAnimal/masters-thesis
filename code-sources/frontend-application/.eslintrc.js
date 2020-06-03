module.exports = {
  extends: [
    "plugin:react/recommended",
    "prettier/@typescript-eslint",
    "plugin:prettier/recommended",
  ],
  parserOptions: {
    ecmaVersion: 2018, // Allows for the parsing of modern ECMAScript features
    sourceType: "module", // Allows for the use of imports
    ecmaFeatures: {
      jsx: true, // Allows for the parsing of JSX
    },
  },
  rules: {
    "semi": "off",
    "arrow-parens": ["error", "as-needed"],
    "no-use-before-define":  ["error", { "functions": false, "classes": false,"variables": false }],
    "prefer-arrow-callback": 1,
    "eqeqeq": "error",
    "max-len": "off",
    "new-parens": "error",
    "no-bitwise": "error",
    "no-console": "off",
    "no-caller": "error",
    "no-multiple-empty-lines": ["error", { "max": 2, "maxEOF": 1, "maxBOF": 0 }],
    "quote-props": ["error", "as-needed"],
    "react/prop-types": "off",
    "sort-imports-es6-autofix/sort-imports-es6": [2, {
      "ignoreCase": false,
      "ignoreMemberSort": false,
      "memberSyntaxSortOrder": ["none", "all", "multiple", "single"]
    }],
    "no-irregular-whitespace": "warn",
  },
  plugins: [
    "sort-imports-es6-autofix"
  ],
  settings: {
    react: {
      version: "detect", // Tells eslint-plugin-react to automatically detect the version of React to use
    },
  },
};

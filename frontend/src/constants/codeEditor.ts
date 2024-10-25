export const LANGUAGE_VERSIONS = {
  javascript: '18.15.0',
  typescript: '5.0.3',
  python: '3.10.0',
  java: '15.0.2',
  csharp: '6.12.0',
  php: '8.2.3',
};

interface CodeSnippets {
  [key: string]: string;
}

export const CODE_SNIPPETS: CodeSnippets = {
  javascript: `\n// JavaScript\nconsole.log("Hello, World!");\n`,
  typescript: `\n// TypeScript\nconsole.log("Hello, World!");\n`,
  python: `\n# Python\nprint("Hello, World!")\n`,
  java: `\n// Java\npublic class Main {\n\tpublic static void main(String[] args) {\n\t\tSystem.out.println("Hello, World!");\n\t}\n}\n`,
  csharp: `\n// C#\nusing System;\n\nclass Program {\n\tstatic void Main() {\n\t\tConsole.WriteLine("Hello, World!");\n\t}\n}\n`,
  php: `<?php\n// PHP\necho "Hello, World!";\n`,
};

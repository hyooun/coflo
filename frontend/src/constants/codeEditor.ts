export const LANGUAGE_VERSIONS = {
  typescript: '5.0.3',
  javascript: '18.15.0',
  python: '3.10.0',
  java: '15.0.2',
  c: '11.2.0',
  cpp: '11.2.0',
  csharp: '6.12.0',
  go: '1.20.2',
  ruby: '3.2.0',
  rust: '1.68.0',
  swift: '5.8.0',
  php: '8.2.3',
  shell: 'bash-5.1',
  sql: 'mysql-8.0',
  html: '5',
  css: '3',
  plaintext: 'text',
};

interface CodeSnippets {
  [key: string]: string;
}

export const CODE_SNIPPETS: CodeSnippets = {
  typescript: `// TypeScript\nconsole.log("Hello, World!");\n`,
  javascript: `// JavaScript
console.log("Hello, World!");
`,
  python: `# Python
print("Hello, World!")
`,
  java: `// Java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
`,
  c: `// C
#include <stdio.h>

int main() {
    printf("Hello, World!\\n");
    return 0;
}
`,
  cpp: `// C++
#include <iostream>

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}
`,
  csharp: `// C#
using System;

class Program {
    static void Main() {
        Console.WriteLine("Hello, World!");
    }
}
`,
  go: `// Go
package main

import "fmt"

func main() {
    fmt.Println("Hello, World!")
}
`,
  ruby: `# Ruby
puts "Hello, World!"
`,
  rust: `// Rust
fn main() {
    println!("Hello, World!");
}
`,
  swift: `// Swift
print("Hello, World!")
`,
  php: `<?php
// PHP
echo "Hello, World!";
`,
  shell: `#!/bin/bash
# Shell
echo "Hello, World!"
`,
  sql: `-- SQL
SELECT 'Hello, World!' AS greeting;
`,
  html: `<!DOCTYPE html>
<html>
<head>
    <title>Hello World</title>
</head>
<body>
    <h1>Hello, World!</h1>
</body>
</html>
`,
  css: `/* CSS */
body {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    margin: 0;
    font-family: sans-serif;
}

h1 {
    color: #333;
    font-size: 2em;
}
`,
  plaintext: `This is a plain text file.
No syntax highlighting needed.
Just write your text here.`,
};

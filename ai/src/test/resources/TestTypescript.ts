function greet(name: string) {
    console.log(`Hello, ${name}!`);
}

const multiply = (a: number, b: number): number => a * b;

class TestClass {
    private message: string;

    constructor(message: string) {
        this.message = message;
    }

    public showMessage() {
        console.log(this.message);
    }

    public static staticMethod() {
        return 'This is a static method';
    }

    public methodWithArrowFunction() {
        const arrowFunction = () => {
            console.log('This is an arrow function inside a method');
        };
        arrowFunction();
    }
}

const anonymousFunction = function() {
    console.log('This is an anonymous function');
};

const simpleArrow = () => console.log('Simple arrow function');

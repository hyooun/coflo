function functionOne() {
    console.log('Function One');
}

const functionTwo = (param) => {
    return param * 2;
};

class TestClass {
    methodOne() {
        console.log('Class Method One');
    }

    static staticMethod() {
        return 'Static Method';
    }

    methodWithArrowFunction() {
        const arrowFunc = () => {
            console.log('Arrow Function Inside Method');
        };
        arrowFunc();
    }
}

const functionThree = function() {
    console.log('Anonymous Function');
};

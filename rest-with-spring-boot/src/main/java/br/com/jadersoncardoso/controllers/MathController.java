package br.com.jadersoncardoso.controllers;

import br.com.jadersoncardoso.exception.UnsupportedMathOperationException;
import br.com.jadersoncardoso.math.SimpleMath;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.jadersoncardoso.utils.NumberUtils.convertToDouble;
import static br.com.jadersoncardoso.utils.NumberUtils.isNumeric;

@RestController
@RequestMapping("/math")
public class MathController {
    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(@PathVariable String numberOne,
                      @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.sum(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping("/subtraction/{numberOne}/{numberTwo}")
    public Double subtraction(@PathVariable String numberOne,
                              @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.subtraction(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping("/multiplication/{numberOne}/{numberTwo}")
    public Double multiplication(@PathVariable String numberOne,
                              @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.multiplication(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping("/division/{numberOne}/{numberTwo}")
    public Double division(@PathVariable String numberOne,
                                 @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.division(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @RequestMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(@PathVariable String numberOne,
                      @PathVariable String numberTwo) {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.mean(convertToDouble(numberOne), convertToDouble(numberTwo));
    }
    @RequestMapping("/squareroot/{number}")
    public Double squareroot(@PathVariable String number) {
        if (!isNumeric(number)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return SimpleMath.squareRoot(convertToDouble(number));
    }


}

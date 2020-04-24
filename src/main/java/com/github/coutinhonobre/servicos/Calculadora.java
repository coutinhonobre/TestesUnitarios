package com.github.coutinhonobre.servicos;

import com.github.coutinhonobre.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {
	
	public int somar(int a, int b) {
		return a + b;
	}

	public int subtrair(int a, int b) {
		return a - b;
	}

	public double dividir(int a, int b) throws NaoPodeDividirPorZeroException {
		
		if(b == 0) throw new NaoPodeDividirPorZeroException();
		
		return a / b;
		
	}

	public double multiplicar(int a, int b) {
		return a * b;
	}

}

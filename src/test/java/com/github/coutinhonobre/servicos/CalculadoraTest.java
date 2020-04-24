package com.github.coutinhonobre.servicos;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.github.coutinhonobre.exceptions.NaoPodeDividirPorZeroException;


public class CalculadoraTest {	
	
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		// cenario	
		int a = 5;
		int b = 3;	
		
		//acao 
		int resultado = calc.somar(a, b);
		
		//verificacao
		assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisNumeros() {
		
		//cenario 
		int a = 8;
		int b = 5;
		
		//acao 
		int resultado = calc.subtrair(a, b);
		
		//verificacao
		assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisNumeros() throws NaoPodeDividirPorZeroException {
		
		//cenario 
		int a = 6;
		int b = 3;
		
		//acao 
		double resultado = calc.dividir(a, b);
		
		//verificacao
		assertEquals(2.0, resultado, 0.0001);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		int a = 10;
		int b = 0;
		
		calc.dividir(a, b);
		
	}
	
	@Test
	public void deveMultiplicarDoisDumeros() {
		
		//cenario
		int a = 2;
		int b = 3;
		
		//acao
		double resultado = calc.multiplicar(a, b);
		
		//verificacao
		assertEquals(6, resultado, 0.0001);
		
	}
	
	
	
	
}

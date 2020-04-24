package com.github.coutinhonobre.servicos;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.coutinhonobre.entidades.Usuario;


public class AssertTest {
	
	@Test
	public void test() {
		assertTrue(true);
		assertFalse(false);
		
		assertEquals("Erro de comparacao",1, 1);
		assertEquals(0.51234, 0.512, 0.001);
		assertEquals(Math.PI, 3.14, 0.01);
		
		/*
		 * O Java conta com diversos Wrappers que adicionam funcionalidades 
		 * a outras classes ou tipos primitivos
		 * Integer i = Integer.valueOf(2);
		 */
		int i = 5;
		Integer i2 = 5;
		
		assertEquals(Integer.valueOf(i), i2);
		assertEquals(i,  i2.intValue());
		
		assertEquals("bola", "bola");
		assertNotEquals("bola", "casa");
		assertTrue("bola".equalsIgnoreCase("Bola"));
		assertTrue("bola".startsWith("bo"));
		
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		
		assertEquals(u1, u2);
		
		// verificar se os objetos sao da mesma instancia
		assertSame(u1, u1);
		assertNotSame(u1, u2);
		
		assertTrue(u3 == null);
		assertNull(u3);
		assertNotNull(u1);
		
		
	}

}

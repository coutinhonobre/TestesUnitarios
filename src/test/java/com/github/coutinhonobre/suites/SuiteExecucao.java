package com.github.coutinhonobre.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.coutinhonobre.servicos.AssertTest;
import com.github.coutinhonobre.servicos.CalculadoraTest;
import com.github.coutinhonobre.servicos.CalculoValorLocacaoTest;
import com.github.coutinhonobre.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class,
	AssertTest.class
})
public class SuiteExecucao {
	//Remova se puder!
}

package com.feivirus.ministatemachine.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.feivirus.ministatemachine.Action;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.util.ReflectionUtil;

public class ActionImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C>{
	private ExecutionContext executionContext;
	
	private Method  method;
	
	public ActionImpl(Method method, ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.method = method;
	}
	
	@Override
	public void execute(S from, S to, E event, C context, T stateMachine) {
		List<Object> paramList = new ArrayList<>();
		
		paramList.add(0, from);
		paramList.add(1, to);
		paramList.add(2, event);
		paramList.add(3, context);
		ReflectionUtil.invoke(method, stateMachine, paramList.toArray());
	}	
}

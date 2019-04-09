package com.feivirus.ministatemachine.impl;

import java.lang.reflect.Method;

import com.feivirus.ministatemachine.Action;
import com.feivirus.ministatemachine.StateMachine;

/**
 * 代理用户的回调方法
 * @author feivirus
 *
 * @param <T>
 * @param <S>
 * @param <E>
 * @param <C>
 */
public class ActionProxyImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements Action<T, S, E, C>{
	private String methodName;
	
	private ExecutionContext executionContext;
	
	//用户的方法
	private Action<T, S, E, C> proxy;
	
	public ActionProxyImpl(String methodName, ExecutionContext executionContext) {
		this.methodName = methodName;
		this.executionContext = executionContext;		
	}
	
	@Override
	public void execute(S from, S to, E event, C context, T stateMachine) {
		getProxy().execute(from, to, event, context, stateMachine);
	}
	
	//设置用户的代理方法
	private Action<T, S, E, C> getProxy() {
		if (proxy == null) {
			Class<?> stateMachineClazz = executionContext.getExecutionTargetType();
			Class<?>[] methodParamTypes = executionContext.getMethodCallParamTypes();
			Method method = StateMachineBuilderImpl.searchMethod(stateMachineClazz, 
					AbstractStateMachine.class, methodName, methodParamTypes);
			
			if (method != null) {
				proxy = FSM.newActionImpl(method, executionContext);
			}			
		}
		return proxy;
	}
}

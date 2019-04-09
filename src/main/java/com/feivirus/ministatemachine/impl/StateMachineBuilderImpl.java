package com.feivirus.ministatemachine.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.feivirus.ministatemachine.SingleTransitionBuilder;
import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.StateMachineBuilder;
import com.feivirus.ministatemachine.util.ReflectionUtil;

public class StateMachineBuilderImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements StateMachineBuilder<T, S, E, C>{
	private Map<S, State<T, S, E, C>> states = new HashMap<>();
	
	private Constructor<? extends T> constructor;
	
	private ExecutionContext executionContext;
	
	//用户定义的状态机的from, to, event, context类型,作为用户定义回调的参数类型,反射调用
	private Class<?>[] actionParamTypes;
	
	private E startEvent, finishEvent, terminateEvent;
	
	private Class<? extends T> stateMachineImplClazz;
	
	public StateMachineBuilderImpl(Class<? extends T> stateMachineImplClazz,
			Class<S> stateClazz, Class<E> eventClazz, Class<C> contextClazz,
			Class<?>...constructorParams) {
		constructor = extractConstructor(stateMachineImplClazz, constructorParams);
		actionParamTypes = new Class<?>[] {stateClazz, stateClazz, eventClazz, contextClazz};
		executionContext = new ExecutionContext(stateMachineImplClazz, actionParamTypes);
		stateMachineImplClazz = stateMachineImplClazz;
	}
	
	private <T> Constructor<? extends T> extractConstructor(Class<T> type, Class<?>[] paramTypes) {
		Constructor<? extends T> constructor = null;
		
		try {
			constructor = ReflectionUtil.getConstructor(type, paramTypes);
		} catch (Exception e) {
			try {
				constructor = ReflectionUtil.getConstructor(type, new Class<?>[0]);
			} catch(Exception ex) {
				
			}
		}
		return constructor;
	}
	
	@Override
	public SingleTransitionBuilder<T, S, E, C> singleTransition() {		
		return FSM.newSingleTransitionBuilder(states, executionContext);
	}

	@Override
	public T newStateMachine(S stateId, Object... constructorParam) {
		if (!valideState(stateId)) {
			throw new IllegalArgumentException("cannot find state " + stateId);
		}
		
		Class<?>[] paramTypes = constructor.getParameterTypes();
		T stateMachine;
		try {
			if (paramTypes == null || 
				paramTypes.length == 0) {
				stateMachine = ReflectionUtil.newInstance(constructor);
			} else {
				stateMachine = ReflectionUtil.newInstance(constructor, constructorParam);
			}
		} catch (Exception e) {
			throw new IllegalStateException("new state machine instance failed" + e.getMessage());
		}		
		
		//状态机初始化开始状态,放到StateMachineData里面
		AbstractStateMachine<T, S, E, C> stateMachineImpl = (AbstractStateMachine<T, S, E, C>)stateMachine;
		stateMachineImpl.postConstruct(stateId, states, new Runnable() {
			
			@Override
			public void run() {
				stateMachineImpl.setStartEvent(startEvent);
				stateMachineImpl.setFinishEvent(finishEvent);
				stateMachineImpl.setTerminateEvent(terminateEvent);
			}
		});
		
		return stateMachine;
	}

	@Override
	public T newStateMachine(S stateId) {
		return newStateMachine(stateId, new Object[0]);
	}	
	
	private boolean valideState(S stateId) {
		if (stateId != null &&
			states.get(stateId) != null) {
			return true;
		}
		return false;
	}
	
	public static Method searchMethod(Class<?> targetClass, Class<?> superClass,
			String methodName, Class<?>[] parameterType) {
		if (superClass.isAssignableFrom(targetClass)) {
			Class<?> clazz = targetClass;
			
			while (!superClass.equals(clazz)) {
				try {
					return clazz.getDeclaredMethod(methodName, parameterType);
				} catch (Exception e) {
					clazz = clazz.getSuperclass();
				}				
			}
		}
		return null;
	}
	
	private void installComponents() {
		
	}
	
	private void installStates() {
		
	}
}

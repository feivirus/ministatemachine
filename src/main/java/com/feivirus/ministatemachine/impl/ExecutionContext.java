package com.feivirus.ministatemachine.impl;

import lombok.Data;

@Data
public class ExecutionContext {
	private Class<?> executionTargetType;
	
	private Class<?>[] methodCallParamTypes;
	
	ExecutionContext(Class<?> executionTargetType, Class<?>[] methodParamTypes) {
		this.executionTargetType = executionTargetType;
		this.methodCallParamTypes = methodParamTypes;
	}
}

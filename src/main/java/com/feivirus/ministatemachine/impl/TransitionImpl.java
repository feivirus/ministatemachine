package com.feivirus.ministatemachine.impl;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import com.feivirus.ministatemachine.Action;
import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateContext;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.Transition;

public class TransitionImpl<T extends StateMachine<T, S, E, C>, S, E, C> implements Transition<T, S, E, C>{
	private List<Action<T, S, E, C>> actions = new ArrayList<>();
	
	private State<T, S, E, C> toState;
	
	private State<T, S, E, C> fromState;
	
	private E event;
	
	protected TransitionImpl() {
	}
	
	@Override
	public void setSourceState(State<T, S, E, C> state) {
		fromState = state;
	}

	@Override
	public void setTargetState(State<T, S, E, C> state) {
		toState = state;
	}

	@Override
	public void setEvent(E event) {
		this.event = event;
	}

	@Override
	public void addAction(Action<T, S, E, C> newAction) {
		actions.add(newAction);
	}

	@Override
	public void internalFire(StateContext<T, S, E, C> stateContext) {
		State<T, S, E, C> fromState = stateContext.getFromState();
		
		//退出老状态
		exitOldState(stateContext.getFromState(), stateContext);
		//切换
		doTransit(fromState, toState, stateContext);
		//进入新状态	
		stateContext.getResult().setAccepted(true).setTargetState(toState);
	}
	
	private void exitOldState(State<T, S, E, C> oldState, StateContext<T, S, E, C> stateContext) {
		//TODO 按A->B->C的状态链条,依次C,B,A退出
		oldState.exit(stateContext);
	}	
	
	private void doTransit(State<T, S, E, C> from, State<T, S, E, C> to, StateContext<T, S, E, C> stateContext) {
		from.exit(stateContext);
		transit(stateContext);
		to.entry(stateContext);
	}

	@Override
	public void exit(StateContext<T, S, E, C> stateContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public State<T, S, E, C> transit(StateContext<T, S, E, C> stateContext) {
		stateContext.getExecutor().begin("TRANSITION__" + this.toString());
		for(Action<T, S, E, C> action : actions) {
			stateContext.getExecutor().defer(action, fromState.getStateId(), 
					toState.getStateId(), stateContext.getEvent(),
					stateContext.getContext(), stateContext.getStateMachine().getThis());
		}
		return toState;
	}

	@Override
	public String toString() {
		return fromState + "-[" + event.toString() + ", " +
			"]->" + toState;
	}	
}

package com.feivirus.ministatemachine.impl;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.feivirus.ministatemachine.ActionExecutionService;
import com.feivirus.ministatemachine.State;
import com.feivirus.ministatemachine.StateContext;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.StateMachineData;
import com.feivirus.ministatemachine.TransitionResult;
import com.feivirus.ministatemachine.util.Pair;

import lombok.Data;

@Data
public abstract class AbstractStateMachine<T extends StateMachine<T, S, E, C>, S, E, C> implements StateMachine<T, S, E, C>{
	
	private volatile StateMachineStatus status = StateMachineStatus.INITIALIZED;	
	
	private LinkedBlockingDeque<Pair<E, C>> queuedEvents = new LinkedBlockingDeque<>();
	
	//处理对queuedEvents的读写
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	private StateMachineData<T, S, E, C> data;
	
	private ActionExecutionService<T, S, E, C> executor = new ExecutionServiceImple<>();
	
	private E startEvent, finishEvent, terminateEvent;
	
	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return getStatus() == StateMachineStatus.IDLE || getStatus() == StateMachineStatus.BUSY;
	}	
	
	@Override
	public void fire(E event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fire(E event, C context) {
		fire(event, context, false);
	}		
	
	/**
	 * 
	 * @param event
	 * @param context
	 * @param insertFirst 在队列头部还是尾部插入事件
	 */
	public void fire(E event, C context, boolean insertFirst) {
		boolean isEntryPoint = isEntryPoint();
		if (isEntryPoint) {
			StateMachineContext.set((T)this);
		} 
		
		try {
			internalFire(event, context, insertFirst);
		} finally {
			if (isEntryPoint) {
				StateMachineContext.set(null);
			}
		}
		
	}
	
	private void internalFire(E event, C context, boolean insertFirst) {
		if (getStatus() == StateMachineStatus.INITIALIZED) {
			start(context);
		}
		if (getStatus() == StateMachineStatus.TERMINATED) {
			throw new IllegalStateException("The state machine is terminated");
		}
		if (getStatus() == StateMachineStatus.ERROR) {
			throw new IllegalStateException("The state machine errored");
		}
		if (insertFirst) {	
			
		} else {
			queuedEvents.addLast(new Pair<E, C>(event, context));
		}
		processEvents();
	}
	
	private boolean isEntryPoint() {
		return StateMachineContext.currentInstance() == null;
	}

	/**
	 * 从持久化数据中进入状态机，一个个读入历史状态,直到最新状态
	 */
	@Override
	public void start(C context) {
		if (isStarted()) {
			return;
		}
		// 读历史状态,在做转换前，先一步步走到最新状态
		setStatus(StateMachineStatus.BUSY);
		internalStart(context, data, executor);
		setStatus(StateMachineStatus.IDLE);
		processEvents();
	}
	
	private void internalStart(C context, StateMachineData<T, S, E, C> oldData, 
			ActionExecutionService<T, S, E, C> executionService) {
		State<T, S, E, C> initialState = oldData.read().initialStateFromUser();
		StateContext<T, S, E, C> stateContext = FSM.newStateContext(this, oldData, initialState, 
				startEvent, context, null, executionService);
		entryAll(initialState, stateContext);
		executionService.execute();
		data.write().currentState(initialState.getStateId());		
	}
	
	private void entryAll(State<T, S, E, C> from, StateContext<T, S, E, C> stateContext) {
		Stack<State<T, S, E, C>> stack = new Stack<>();
		State<T, S, E, C> state = from;
		
		//TODO 依次处理历史状态		
		stack.push(state);			
	
		while (stack.size() > 0) {
			state = stack.pop();
			state.entry(stateContext);
		}
	}
	
	private void processEvents() {
		if (getStatus() != StateMachineStatus.BUSY) {
			rwLock.writeLock().lock();
			setStatus(StateMachineStatus.BUSY);
			
			try {
				Pair<E, C> eventPair = null;
				E event = null;
				C context = null;
				while ((eventPair = queuedEvents.poll()) != null) {
					if (Thread.interrupted()) {
						queuedEvents.clear();
						break;
					}
					event = eventPair.key();
					context = eventPair.value();
					processEvent(event, context, data, executor);					
				}
				
				
			} finally {
				if (getStatus() == StateMachineStatus.BUSY) {
					setStatus(StateMachineStatus.IDLE);
				}
				rwLock.writeLock().unlock();
			}
			
		}
	}
	
	private boolean processEvent(E event, C context, StateMachineData<T, S, E, C> oldData,
			ActionExecutionService<T, S, E, C> executionService) {
		StateMachineData<T, S, E, C> localData = oldData;
		State<T, S, E, C> fromState = localData.read().currentStateFromUser();
		S fromStateId = fromState.getStateId();
		S toStateId = null;
		
		try {
			TransitionResult<T, S, E, C> result = FSM.newResult(false, fromState);
			StateContext<T, S, E, C> stateContext = FSM.newStateContext(this, localData, fromState, 
					event, context, result, executionService);
			
			fromState.internalFire(stateContext);
			toStateId = result.getTargetState().getStateId();
			
			//接受状态转换，调用用户提供的回调方法
			if (result.isAccepted()) {
				executionService.execute();
				localData.write().currentState(toStateId);
				return true;
			}			
		} catch (Exception ex) {
			
		}finally {
			
		}
		return false;
	}

	@Override
	public T getThis() {
		return (T)this;
	}	
	
	void postConstruct(S initialState, Map<S, ? extends State<T, S, E, C>> states, Runnable runnable) {
		data = FSM.newStateMachineData(states);
		data.write().initialState(initialState);
		data.write().currentState(null);
	}
}

package com.feivirus.ministatemachine.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.feivirus.ministatemachine.Action;
import com.feivirus.ministatemachine.ActionExecutionService;
import com.feivirus.ministatemachine.StateMachine;
import com.feivirus.ministatemachine.util.Pair;

public abstract class AbstractExecutionService<T extends StateMachine<T, S, E, C>, S, E, C> implements ActionExecutionService<T, S, E, C>{
	protected LinkedList<Pair<String, List<ActionContext<T, S, E, C>>>> actionBuckets = 
			new LinkedList<>();
	
	private int totalActionSize = 0;
	
	@Override
	public void begin(String bucketName) {
		List<ActionContext<T, S, E, C>> actionContexts = new ArrayList<ActionContext<T, S, E, C>>();
		actionBuckets.add(new Pair<String, List<ActionContext<T, S, E, C>>>(bucketName, actionContexts));
	}

	@Override
	public void execute() {
		try {
			while (actionBuckets.size() > 0) {
				Pair<String, List<ActionContext<T, S, E, C>>> actionBucket = actionBuckets.poll();				
				String bucketName = actionBucket.key();
				List<ActionContext<T, S, E, C>> actionContexts = actionBucket.value();
				
				doExecute(bucketName, actionContexts);
			}
		} catch (Exception e) {
			
		}
	}

	@Override
	public void defer(Action<T, S, E, C> action, S from, S to, E event, C context, T stateMachine) {
		List<ActionContext<T, S, E, C>> actions = actionBuckets.peekLast().value();
		
		actions.add(ActionContext.get(action, from, to, event, context, stateMachine, totalActionSize++));
	}
	
	private void doExecute(String bucketName, List<ActionContext<T, S, E, C>> actionContexts) {
		for(int i = 0; i < actionContexts.size(); i++) {
			ActionContext<T, S, E, C> context = actionContexts.get(i);
			
			if (context != null) {
				context.run();
			}
		}
	}
	
	static class ActionContext<T extends StateMachine<T, S, E, C>, S, E, C> {
		Action<T, S, E, C> action;
		S from;
		S to;
		E event;
		C context;
		T fsm;
		int position;
		
		private ActionContext(Action<T, S, E, C> action, S from, S to, E event, C context, T fsm, int position) {
			super();
			this.action = action;
			this.from = from;
			this.to = to;
			this.event = event;
			this.context = context;
			this.fsm = fsm;
			this.position = position;
		}
		
		static <T extends StateMachine<T, S, E, C>, S, E, C> ActionContext<T, S, E, C> get(
				Action<T, S, E, C> action, S from, S to, E event, C context, T fsm, int position) {
			return new ActionContext<T, S, E, C>(action, from, to, event, context, fsm, position);
		}
		
		void run() {
			action.execute(from, to, event, context, fsm);
		}
	}
	
}

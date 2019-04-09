package com.feivirus.ministatemachine;

import com.feivirus.ministatemachine.annotation.StateMachineBinder;
import com.feivirus.ministatemachine.impl.AbstractStateMachine;
import com.feivirus.ministatemachine.impl.StateMachineBuilderImpl;

/**
 * 
 * 自动机
 * @author feivirus
 * 应用:比如订单状态切换,订单状态/合同规则状态切换,词法分析，审批工作流 
 */
public class StateMachineMain {
	enum SampleEvent {
		ToA, ToB, ToC, ToD
	}
	
	//TODO 通过注解生成状态机
	//@StateMachineBinder(stateType = String.class, eventType = SampleEvent.class, contextType = Integer.class)
	private static class StateMachineSample extends AbstractStateMachine<SingleStateMachine, Object, Object, Object> {
		protected void fromAToB(String from, String to, SampleEvent event, Integer context) {
			System.out.println("Transition from " + from + " to " + to + " on event "
					+ event + " with context " + context);
		}	
		
		protected void fromBToC(String from, String to, SampleEvent event, Integer context) {
			System.out.println("Transition from " + from + " to " + to + " on event "
					+ event + " with context " + context);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		StateMachineBuilder builder = new StateMachineBuilderImpl(StateMachineSample.class, String.class, SampleEvent.class, Integer.class);
		builder.singleTransition().from("A").to("B").on(SampleEvent.ToB).callMethod("fromAToB");
		builder.singleTransition().from("B").to("C").on(SampleEvent.ToC).callMethod("fromBToC");
		
		StateMachine ssm = builder.newStateMachine("A");
		ssm.fire(SampleEvent.ToB, 10);
		ssm.fire(SampleEvent.ToC, 20);
	}
}

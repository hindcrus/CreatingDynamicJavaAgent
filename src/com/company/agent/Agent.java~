package com.company.agent;

import java.lang.instrument.Instrumentation;

/**
 * Created by chetan on 2/7/16.
 */
public class Agent {
public static void premain(String agentArgs, Instrumentation inst) {
	 System.out.println("Starting the agent");
	 inst.addTransformer(new ImportantLogClassTransformer());
}
}

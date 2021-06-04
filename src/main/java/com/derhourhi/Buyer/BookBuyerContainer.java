package com.derhourhi.Buyer;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class BookBuyerContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime=Runtime.instance(); 
            ProfileImpl profileImpl=new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
            AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
            //AgentController agentController=agentContainer.createNewAgent("buyer1", "com.derhourhi.BookBuyerAgent", new Object[]{});
            AgentController agentController=agentContainer.createNewAgent("buyer2", "com.derhourhi.Buyer.BookBuyerAgent", new Object[]{"XML"});
            agentController.start();
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}

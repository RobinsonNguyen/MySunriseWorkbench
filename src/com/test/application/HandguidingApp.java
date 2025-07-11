package com.test.application;


import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.motionModel.HRCMotions;
import com.kuka.roboticsAPI.motionModel.HandGuidingMotion;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.med.deviceModel.LBRMed;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class HandguidingApp extends RoboticsAPIApplication {
	@Inject
	private LBRMed robot;
	private HandGuidingMotion _handguiding;

	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		// your application execution starts here
		robot.move(HRCMotions.handGuiding());
		
		_handguiding = new HandGuidingMotion();
		_handguiding.setJointLimitsMax(Math.toRadians(100),Math.toRadians(100),Math.toRadians(100),
				Math.toRadians(100),Math.toRadians(100),Math.toRadians(100));
		_handguiding.setJointLimitsMin(Math.toRadians(-100),Math.toRadians(-100),Math.toRadians(-100),
				Math.toRadians(-100),Math.toRadians(-100),Math.toRadians(-100));
		_handguiding.setJointVelocityLimit(3); //rad/s
		_handguiding.setCartVelocityLimit(100); //mm/s
		
		testMethod();
	}

	private void testMethod() {
		int ret = 0;
		while(ret != 2){
			getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Option?", "Param","Default","exit");
			
			switch(ret){
			case 0:
				getLogger().info("handguiding with params");
				robot.move(_handguiding);
				break;
			case 1:
				getLogger().info("default core-handguiding");
				robot.move(HRCMotions.handGuiding());
				break;
			case 2:
				break;
			}
		}
	}
}
               


















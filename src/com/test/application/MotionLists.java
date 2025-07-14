package com.test.application;


import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;

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
public class MotionLists extends RoboticsAPIApplication {
	@Inject
	private LBRMed robot;

	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		// your application execution starts here
		double override = 0.5;
		getApplicationControl().setApplicationOverride(override);
		ObjectFrame P1 = getApplicationData().getFrame("/P1");
		ObjectFrame P2 = getApplicationData().getFrame("/P2");
		ObjectFrame P3 = getApplicationData().getFrame("/P2/P3");
		
		List<IMotionContainer> motionList = new LinkedList<IMotionContainer>();
		motionList.add(robot.move(ptp(P1).setJointVelocityRel(0.3)));	
		motionList.add(robot.move(ptp(P2)));
		motionList.add(robot.move(ptp(P3)));
	}
}
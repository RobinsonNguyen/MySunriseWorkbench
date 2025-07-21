package com.test.application;


import java.util.List;

import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.deviceModel.Device;
import com.kuka.roboticsAPI.motionModel.ErrorHandlingAction;
import com.kuka.roboticsAPI.motionModel.IErrorHandler;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.common.ThreadUtil;
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
public class Motions extends RoboticsAPIApplication {
	@Inject
	private LBRMed robot;
	private ForceSensorData sensorData;
	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		// your application execution starts here
		IErrorHandler errorHandler = new IErrorHandler(){

			@Override
			public ErrorHandlingAction handleError(Device device,
					IMotionContainer failedContainer,
					List<IMotionContainer> canceledContainers) {
				// TODO Auto-generated method stub
				getLogger().info("Failed to command, " + failedContainer.getCommand().toString()); 
				return ErrorHandlingAction.EndApplication;
			}
		};
		
		getApplicationControl().registerMoveAsyncErrorHandler(errorHandler);
		double override = 0.4;
		getApplicationControl().setApplicationOverride(override);
		IMotionContainer imc = robot.move(ptp(getApplicationData().getFrame("/P3")).setJointVelocityRel(0.4));
		goHome();
		while(!imc.isFinished()){
			sensorData = robot.getExternalForceTorque(robot.getFlange());
			double zForce = sensorData.getForce().getZ();
			
			if(Math.abs(zForce) > 10){
				getLogger().info("Force in Z vector" + zForce);
				imc.cancel();
			}
			
			// Slow the processor so the CPU doesn't get taxed
			ThreadUtil.milliSleep(50);
		}
		
		
	}
	
	private void goHome() {
		int ret = getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Go home?", "Yes","No");
		switch(ret){
		case 0:
			getLogger().info("Going home...");
			robot.move(ptpHome().setJointVelocityRel(0.4));
			break;
		case 1:
			getLogger().info("Staying here");
			break;
		}
	}
}
package com.test.application;


import javax.inject.Inject;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.motionModel.HRCMotions;
import com.kuka.roboticsAPI.sensorModel.DataRecorder;
import com.kuka.roboticsAPI.sensorModel.ForceSensorData;
import com.kuka.roboticsAPI.sensorModel.StartRecordingAction;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.common.ThreadUtil;
import com.kuka.med.deviceModel.LBRMed;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import java.io.FileWriter;
import java.io.File;

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
public class ScaleForceTest extends RoboticsAPIApplication {
	@Inject
	private LBRMed robot;
	private ForceSensorData sensorData;
	private Tool ScaleForceFixture;

	@Override
	public void initialize() {
		// initialize your application here
		ScaleForceFixture.attachTo(robot.getFlange());
	}

	@Override
	public void run() {
		// your application execution starts here
		moveStartScaleForce();
		for (int i = 0; i < 100; i++) {
			sensorData = robot.getExternalForceTorque(ScaleForceFixture.getFrame("/TCP"));
			double xForce = sensorData.getForce().getX();
			double yForce = sensorData.getForce().getY();
			double zForce = sensorData.getForce().getZ();
			getLogger().info("Force in Z vector: " + zForce+"\n then X: "+xForce+" \nthen Y: "+yForce);
			ThreadUtil.milliSleep(2000);
		}
		
		////////////////////////////////////////////////
		//DataRecorder rec_1 = new DataRecorder("Recording_1.log",5,TimeUnit.SECONDS,100);
//		DataRecorder rec = new DataRecorder();
//		rec.setFileName("ScaleTest.log");
//		rec.setSampleInterval(2);
//		rec.addCartesianForce(robot.getFlange(), null);
//		//StartRecordingAction startAction = new StartRecordingAction(rec);
//		//ForceCondition startCondition = ForceCondition.createSpatialForceCondition(getApplicationData().getFrame("/P3"), 2.0);
//		getLogger().info("Starting recording...");
//		rec.startRecording();
//		ThreadUtil.milliSleep(5000);
//		getLogger().info("Stop recording...");
//		rec.stopRecording();
		///////////////////////////////////////
//		getLogger().info("Starting test...");
//		try {
//			File myObj = new File("testlog.txt");
//			if(myObj.createNewFile()){
//				getLogger().info("File was created");
//			}
//			FileWriter myWriter = new FileWriter("testlog.txt");
//			myWriter.write("first test line");
//			myWriter.write("second test line");
//			myWriter.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			getLogger().info("There was an error: " + e.toString());
//			
//		}
	}
	private void moveStartScaleForce() {
		int ret = 0;
		while(ret != 1){
			getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION, "Option?", "Yes","No");
			
			switch(ret){
			case 0:
				getLogger().info("Moving to start frame...");
				robot.move(ptp(getApplicationData().getFrame("StartScaleForce")).setJointVelocityRel(0.4));
				break;
			case 1:
				getLogger().info("doing nothing");
				break;
			}
		}
	}
}
	
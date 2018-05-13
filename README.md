## FTC-PID-Tuner has been replaced with [FTC-OpMode-Tuner](https://github.com/OpenFTC/FTC-OpMode-Tuner)

This is a tool for tuning PID loops on FTC robots in real time. Updated values are sent over UDP to the robot controller every 50ms.

![screenshot here](https://github.com/OpenFTC/FTC-PID-Tuner/raw/master/main-screenshot.png)

## Setup instructions

 1. Download the pre-built jar from [here](https://github.com/OpenFTC/FTC-PID-Tuner/raw/master/out/artifacts/JavaFXApp/FTC-PID-Tuner-0.2-alpha.jar)
 2. Run it by either double-clicking or from the command line with `java -jar nameOfFile.jar`, whichever way you prefer. Note: if using Linux, you may need to run `chmod +x nameOfFile.jar` first.
 3. Copy and paste [the UDP receiver](https://github.com/OpenFTC/FTC-PID-Tuner/blob/master/src/net/frogbots/ftcpidtuner/PidUdpReceiver.java) into a new class in your FTC SDK project
 4. Create a new OpMode. A sample OpMode for reading the incoming data is provided at the end of this README
 5. Connect your computer to the RC phone's network
 6. Input the RC phone's IP address into the dialog of the desktop application
 7. Press the "Begin transmission" button
 8. Run the OpMode. You should see the values printed to telemetry change as you adjust the sliders in the desktop application.

## Sample OpMode to read the incoming packets:

```java
/*
 * Copyright (c) 2017 FTC team 4634 FROGbots
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.net.frogbots.opmodes.Extended.utils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.net.frogbots.utils.PidUdpReceiver;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@TeleOp(name="PID test 2", group="utils")
public class PIDtest2 extends LinearOpMode
{
    private double p, i, d;
    private PidUdpReceiver pidUdpReceiver;

    @Override
    public void runOpMode() throws InterruptedException
    {
        /*
         * Initialize the network receiver
         */
        pidUdpReceiver = new PidUdpReceiver();
        pidUdpReceiver.beginListening();

        telemetry.setMsTransmissionInterval(50);
        waitForStart();

        /*
         * Main loop
         */
        while (opModeIsActive())
        {
            updateCoefficients();

            telemetry.addData("P", formatVal(p));
            telemetry.addData("I", formatVal(i));
            telemetry.addData("D", formatVal(d));
            telemetry.update();
        }

        /*
         * Make sure to call this at the end of your OpMode or else
         * the receiver won't work again until the app is restarted
         */
        pidUdpReceiver.shutdown();
    }

    private void updateCoefficients()
    {
        p = pidUdpReceiver.getP();
        i = pidUdpReceiver.getI();
        d = pidUdpReceiver.getD();
    }

    /*
     * This method formats a raw double for nice display on the DS telemetry
     */
    private String formatVal(double val)
    {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(val);
    }
}
```

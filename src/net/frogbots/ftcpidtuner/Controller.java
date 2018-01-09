/*
 * Copyright (c) 2018 FTC team 4634 FROGbots
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

package net.frogbots.ftcpidtuner;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.beans.property.SimpleObjectProperty;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller
{
    @FXML
    private Slider pSlider;

    @FXML
    private Slider iSlider;

    @FXML
    private Slider dSlider;

    @FXML
    private Label pVal;

    @FXML
    private Label iVal;

    @FXML
    private Label dVal;

    @FXML
    private Button startStopBtn;

    private ScheduledExecutorService timer;

    private ObjectProperty<String> pValProperty;
    private ObjectProperty<String> iValProperty;
    private ObjectProperty<String> dValProperty;

    private DataPacket dataPacket = new DataPacket();

    private InetAddress address = null;
    private DatagramSocket datagramSocket = null;

    private boolean transmissionStatus = false;

    private void init() {
        pValProperty = new SimpleObjectProperty<>();
        pVal.textProperty().bind(pValProperty);

        iValProperty = new SimpleObjectProperty<>();
        iVal.textProperty().bind(iValProperty);

        dValProperty = new SimpleObjectProperty<>();
        dVal.textProperty().bind(dValProperty);

        try {
            address = InetAddress.getByName(StaticVars.targetIP);
            datagramSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStartStopBtnPress()
    {
        if(!transmissionStatus)
        {
            /*
             * We are not currently transmitting, so begin
             */
            startStopBtn.setText("Stop transmission");
            beginScheduledThread();
            transmissionStatus = true;
        }
        else
        {
            /*
             * We are currently transmitting, so stop
             */
            startStopBtn.setText("Begin transmission");
            stopScheduledThread();
            transmissionStatus = false;
            datagramSocket.close();
        }
    }

    private void beginScheduledThread()
    {
        init();

        Runnable transmitter = new Runnable()
        {
            @Override
            public void run()
            {
                dataPacket.setP(pSlider.getValue());
                dataPacket.setI(iSlider.getValue());
                dataPacket.setD(dSlider.getValue());

                Utils.onFXThread(pValProperty, formatDoubleForUiDisplay(dataPacket.getP()));
                Utils.onFXThread(iValProperty, formatDoubleForUiDisplay(dataPacket.getI()));
                Utils.onFXThread(dValProperty, formatDoubleForUiDisplay(dataPacket.getD()));

                sendPacket();
            }
        };

        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(transmitter, 0, 50, TimeUnit.MILLISECONDS);
    }

    private String formatDoubleForUiDisplay(double doubleValue)
    {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(doubleValue);
    }

    private void sendPacket()
    {
        try
        {
            byte[] buffer = dataPacket.toByteArr();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8087);
            datagramSocket.send(packet);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void stopScheduledThread()
    {
        this.timer.shutdown();
        try
        {
            this.timer.awaitTermination(100, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    void exit()
    {
        if(timer != null)
        {
            stopScheduledThread();
        }
        if(datagramSocket != null)
        {
            datagramSocket.close();
        }
    }
}

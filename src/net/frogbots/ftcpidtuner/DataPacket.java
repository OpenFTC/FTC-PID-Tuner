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

import java.nio.ByteBuffer;

class DataPacket
{
    private double p, i, d;

    void setP(double p)
    {
        this.p = p;
    }

    void setI(double i)
    {
        this.i = i;
    }

    void setD(double d)
    {
        this.d = d;
    }

    double getP()
    {
        return p;
    }

    double getI()
    {
        return i;
    }

    double getD()
    {
        return d;
    }

    byte[] toByteArr()
    {
        byte[] packet = new byte[24];

        byte[] pBytes = new byte[8];
        byte[] iBytes = new byte[8];
        byte[] dBytes = new byte[8];
        ByteBuffer.wrap(pBytes).putDouble(p);
        ByteBuffer.wrap(iBytes).putDouble(i);
        ByteBuffer.wrap(dBytes).putDouble(d);

        System.arraycopy(pBytes, 0, packet, 0, pBytes.length);
        System.arraycopy(iBytes, 0, packet, 8, iBytes.length);
        System.arraycopy(dBytes, 0, packet, 16, dBytes.length);

        return packet;
    }
}

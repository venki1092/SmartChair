using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO.Ports;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using uPLibrary.Networking.M2Mqtt;

namespace GallileoInterface
{
    class Program
    {
        public static MqttClient client;
        public static int i = 0;
        public static void Main()
        {
            client = new MqttClient("54.186.96.23", 2882, false, null, null, MqttSslProtocols.None, null);

            client.Connect("gallileo");

            SerialPort mySerialPort = new SerialPort("COM6");

            mySerialPort.BaudRate = 9600;
            mySerialPort.Parity = Parity.None;
            mySerialPort.StopBits = StopBits.One;
            mySerialPort.DataBits = 8;
            mySerialPort.Handshake = Handshake.None;

            mySerialPort.DataReceived += new SerialDataReceivedEventHandler(DataReceivedHandler);

            mySerialPort.Open();

            Console.WriteLine("Press any key to continue...");
            Console.WriteLine();
            Console.ReadKey();
            mySerialPort.Close();
        }

        private static void DataReceivedHandler(object sender, SerialDataReceivedEventArgs e)
        {
            SerialPort sp = (SerialPort)sender;
            string indata = sp.ReadExisting();
            if (i !=0)
            {  
                Debug.Print("Data Received:");
                String data = "{\"distance1:\"" + indata + ",\"distance2:\"" + indata + "}";
                client.Publish("/smartchair/data/gallileo", Encoding.ASCII.GetBytes(data));
                Debug.Print(indata);
            }
            i = 1;
        }
    }
}

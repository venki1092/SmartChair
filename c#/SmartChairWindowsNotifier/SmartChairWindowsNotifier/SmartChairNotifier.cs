using Hardcodet.Wpf.TaskbarNotification;
using Newtonsoft.Json;
using Samples;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Media;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using TranslatorService.Speech;
using uPLibrary.Networking.M2Mqtt;
//using TranslatorService.Speech;

namespace SmartChairWindowsNotifier
{
    class SmartChairNotifier
    {
        private TaskbarIcon notifyIcon;
        private MqttClient client;
        
        public SmartChairNotifier()
        {
            notifyIcon = new TaskbarIcon();
            notifyIcon.Icon = Properties.Resources.Led;
            notifyIcon.ToolTipText = "Left-click to open popup";
            notifyIcon.Visibility = System.Windows.Visibility.Visible;

            notifyIcon.TrayPopup = new FancyPopup();
            //IPAddress ip = new IPAddress()
            //tcp://54.187.143.223:2880
            System.Net.IPAddress ipaddress = System.Net.IPAddress.Parse("54.186.96.23");
           // client = new MqttClient(ipaddress,2882,false,null,null, MqttSslProtocols.None);
            client = new MqttClient("ec2-54-186-96-23.us-west-2.compute.amazonaws.com", 2882 ,false,null,null,MqttSslProtocols.None,null);
            client.MqttMsgPublishReceived += Client_MqttMsgPublishReceived;
            string clientId = Guid.NewGuid().ToString();
            client.Connect("test");
            // subscribe to the topic "/home/temperature" with QoS 2
            client.Subscribe(new string[] { "/smartchair/alert" }, new byte[] { uPLibrary.Networking.M2Mqtt.Messages.MqttMsgBase.QOS_LEVEL_EXACTLY_ONCE });
    ///        SpeechSynthesizer speech = new SpeechSynthesizer(CLIENT_ID, CLIENT_SECRET);
    //        speech.SpeakAsync("This is a beautiful day!");
           //  notifyIcon.ShowCustomBalloon(new FancyPopup(), System.Windows.Controls.Primitives.PopupAnimation.Fade, 2000);
        }

        private void Client_MqttMsgPublishReceived(object sender, uPLibrary.Networking.M2Mqtt.Messages.MqttMsgPublishEventArgs e)
        {
            String mqttMessage = System.Text.Encoding.UTF8.GetString(e.Message);
            SmartChairAlert sca = JsonConvert.DeserializeObject<SmartChairAlert>(mqttMessage);
           // SystemSounds.Asterisk.Play();
            Trace.WriteLine(sca.isHarmful + " " + sca.position + " " + sca.suggestion);
            notifyIcon.ShowBalloonTip("Smart Chair Notification", sca.suggestion, Properties.Resources.Led);

            SpeechSynthesizer speech = new SpeechSynthesizer("venki1092", "Is5vSwR2cmAfIBivcxmdRYWGA0J00XbuOvUzEJa0+mg=");
            speech.SpeakAsync("please check your chair posture");
            //notifyIcon.ShowCustomBalloon(new FancyPopup(), System.Windows.Controls.Primitives.PopupAnimation.Fade, 2000);
        }

        public void displayTaskBarNotifier()
        {

        }
    }
    class SmartChairAlert
    {
        public String isHarmful;
        public String position;
        public String suggestion;
    }
}

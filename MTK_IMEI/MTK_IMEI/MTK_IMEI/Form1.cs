using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Diagnostics;

namespace MTK_IMEI
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void cancelBtn_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void generateBtn_Click(object sender, EventArgs e)
        {
            if (initImei.Text.ToString().Length != 15)
            {
                MessageBox.Show("初始IMEI错误，请重新输入！", "错误！");
                initImei.Text = "";
                return;
            }

            generateBtn.Enabled = false;

            string tempimei = "";
            string imei = "";
            int temp = 0;

            string savePath = System.Windows.Forms.Application.StartupPath + "/imei";
            if (!Directory.Exists(savePath))
            {
                Directory.CreateDirectory(savePath);
            }
            string AfilePath = savePath + "/imei.txt";

            long num = Int64.Parse(initImei.Text.ToString().Substring(0, 14), System.Globalization.NumberStyles.Integer);
            int step = Int16.Parse(stepEditText.Text.ToString(), System.Globalization.NumberStyles.Integer);
            int sum = Int16.Parse(sumEditText.Text.ToString(), System.Globalization.NumberStyles.Integer);

            if (!sameImei.Checked)
            {
                sum = sum * 2;
            }
            string tempimei2 = "";

            int[] d = new int[2];
            for (int i = 0; i < sum; i++)
            {
                tempimei = "";
                if (i != 0)
                {
                    num += step;
                }
                tempimei = Convert.ToString(num, 10);

                temp = 0;
                for (int j = 0; j < 7; j++)  //计算校验位
                {
                    d[0] = tempimei[j * 2] - 0x30;
                    d[1] = (tempimei[j * 2 + 1] - 0x30) * 2;
                    temp = temp + d[0] + (d[1] / 10) + (d[1] % 10);
                }
                temp = 10 - (temp % 10);
                if (temp == 10)
                {
                    temp = 0;
                }
                tempimei += Convert.ToString(temp, 10);
                imei += tempimei + Environment.NewLine;

                if (!sameImei.Checked)
                {
                    if (i % 2 == 0)
                    {
                        tempimei2 = tempimei;
                    }
                    else
                    {
                        generateMPBFile(tempimei, tempimei2, i/2 + 1);
                    }
                }
                else
                {
                    generateMPBFile(tempimei, tempimei, i + 1);
                }
            }
            FileStream fs = new FileStream(AfilePath, FileMode.Create);
            byte[] writeMesaage = Encoding.GetEncoding("GB2312").GetBytes(imei);
            fs.Write(writeMesaage, 0, writeMesaage.Length);
            fs.Close();

            generateBtn.Enabled = true;

            MessageBox.Show("生成成功！", "消息");
        }

        private void generateMPBFile(string imei1, string imei2, int index)
        {
            string file = System.Windows.Forms.Application.StartupPath + "/MP0B_001_NEW";
            if (System.IO.File.Exists(file))
            {
                File.Delete(file);
            }

            System.Diagnostics.ProcessStartInfo startImeiExe = new System.Diagnostics.ProcessStartInfo();
            startImeiExe.FileName = "imei.exe";
            startImeiExe.CreateNoWindow = true;
            startImeiExe.UseShellExecute = false;
            startImeiExe.Arguments = imei1 + " " + imei2;
            System.Diagnostics.Process exep = System.Diagnostics.Process.Start(startImeiExe);
            exep.WaitForExit();

            string tempFileName = "MP0B_001" + "_" + index.ToString();
            file = System.Windows.Forms.Application.StartupPath + "/imei/" + tempFileName;
            if (System.IO.File.Exists(file))
            {
                File.Delete(file);
            }

            File.Move("MP0B_001_NEW", "imei/" + tempFileName);
        }
    }
}

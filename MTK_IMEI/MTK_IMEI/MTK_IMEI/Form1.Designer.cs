namespace MTK_IMEI
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.generateBtn = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.initImei = new System.Windows.Forms.TextBox();
            this.stepEditText = new System.Windows.Forms.TextBox();
            this.sumEditText = new System.Windows.Forms.TextBox();
            this.cancelBtn = new System.Windows.Forms.Button();
            this.sameImei = new System.Windows.Forms.CheckBox();
            this.SuspendLayout();
            // 
            // generateBtn
            // 
            this.generateBtn.Location = new System.Drawing.Point(152, 155);
            this.generateBtn.Name = "generateBtn";
            this.generateBtn.Size = new System.Drawing.Size(72, 23);
            this.generateBtn.TabIndex = 0;
            this.generateBtn.Text = "生成";
            this.generateBtn.UseVisualStyleBackColor = true;
            this.generateBtn.Click += new System.EventHandler(this.generateBtn_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 22);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 12);
            this.label1.TabIndex = 1;
            this.label1.Text = "初始IMEI：";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 51);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(65, 12);
            this.label2.TabIndex = 2;
            this.label2.Text = "递增数量：";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(12, 87);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(65, 12);
            this.label3.TabIndex = 3;
            this.label3.Text = "生成数量：";
            // 
            // initImei
            // 
            this.initImei.Location = new System.Drawing.Point(71, 22);
            this.initImei.Name = "initImei";
            this.initImei.Size = new System.Drawing.Size(153, 21);
            this.initImei.TabIndex = 4;
            // 
            // stepEditText
            // 
            this.stepEditText.Location = new System.Drawing.Point(71, 51);
            this.stepEditText.Name = "stepEditText";
            this.stepEditText.Size = new System.Drawing.Size(153, 21);
            this.stepEditText.TabIndex = 5;
            this.stepEditText.Text = "1";
            // 
            // sumEditText
            // 
            this.sumEditText.Location = new System.Drawing.Point(71, 78);
            this.sumEditText.Name = "sumEditText";
            this.sumEditText.Size = new System.Drawing.Size(153, 21);
            this.sumEditText.TabIndex = 6;
            this.sumEditText.Text = "100";
            // 
            // cancelBtn
            // 
            this.cancelBtn.Location = new System.Drawing.Point(12, 155);
            this.cancelBtn.Name = "cancelBtn";
            this.cancelBtn.Size = new System.Drawing.Size(73, 23);
            this.cancelBtn.TabIndex = 7;
            this.cancelBtn.Text = "取消";
            this.cancelBtn.UseVisualStyleBackColor = true;
            this.cancelBtn.Click += new System.EventHandler(this.cancelBtn_Click);
            // 
            // sameImei
            // 
            this.sameImei.AutoSize = true;
            this.sameImei.Checked = true;
            this.sameImei.CheckState = System.Windows.Forms.CheckState.Checked;
            this.sameImei.Location = new System.Drawing.Point(146, 120);
            this.sameImei.Name = "sameImei";
            this.sameImei.Size = new System.Drawing.Size(72, 16);
            this.sameImei.TabIndex = 8;
            this.sameImei.Text = "两号相同";
            this.sameImei.UseVisualStyleBackColor = true;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(240, 190);
            this.Controls.Add(this.sameImei);
            this.Controls.Add(this.cancelBtn);
            this.Controls.Add(this.sumEditText);
            this.Controls.Add(this.stepEditText);
            this.Controls.Add(this.initImei);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.generateBtn);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "MTK_IMEI";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button generateBtn;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox initImei;
        private System.Windows.Forms.TextBox stepEditText;
        private System.Windows.Forms.TextBox sumEditText;
        private System.Windows.Forms.Button cancelBtn;
        private System.Windows.Forms.CheckBox sameImei;
    }
}


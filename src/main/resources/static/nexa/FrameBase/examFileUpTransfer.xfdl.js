(function()
{
    return function()
    {
        if (!this._is_form)
            return;
        
        var obj = null;
        
        this.on_create = function()
        {
            this.set_name("sample_fileuptransfer_01");
            this.set_titletext("New Form");
            this.set_border("");
            this.set_background("");
            if (Form == this.constructor)
            {
                this._setFormPosition(1024,768);
            }
            
            // Object(Dataset, ExcelExportObject) Initialize
            obj = new Dataset("Dataset00", this);
            obj._setContents("<ColumnInfo><Column id=\"name\" type=\"STRING\" size=\"256\"/><Column id=\"size\" type=\"STRING\" size=\"256\"/></ColumnInfo>");
            this.addChild(obj.name, obj);


            obj = new FileDialog("FileDialog00", this);
            this.addChild(obj.name, obj);


            obj = new FileUpTransfer("FileUpTransfer00", this);
            this.addChild(obj.name, obj);
            
            // UI Components Initialize
            obj = new Static("Static00","20","80","482","150",null,null,null,null,null,null,this);
            obj.set_taborder("2");
            obj.set_text("Drop Files Here");
            obj.set_visible("true");
            obj.set_background("aliceblue");
            obj.set_color("dodgerblue");
            obj.set_font("normal 30pt/normal \"Arial\"");
            obj.set_textAlign("center");
            obj.set_verticalAlign("middle");
            this.addChild(obj.name, obj);

            obj = new Button("Button00","20","20","120","50",null,null,null,null,null,null,this);
            obj.set_taborder("0");
            obj.set_text("open ex");
            this.addChild(obj.name, obj);

            obj = new Grid("Grid00","20","80","482","150",null,null,null,null,null,null,this);
            obj.set_taborder("1");
            obj.set_binddataset("Dataset00");
            obj._setContents("<Formats><Format id=\"default\"><Columns><Column size=\"380\"/><Column size=\"100\"/></Columns><Rows><Row size=\"24\" band=\"head\"/><Row size=\"24\"/></Rows><Band id=\"head\"><Cell text=\"name\"/><Cell col=\"1\" text=\"size\"/></Band><Band id=\"body\"><Cell text=\"bind:name\"/><Cell col=\"1\" text=\"bind:size\" textAlign=\"right\"/></Band></Format></Formats>");
            this.addChild(obj.name, obj);

            obj = new Button("Button01","150","20","120","50",null,null,null,null,null,null,this);
            obj.set_taborder("3");
            obj.set_text("upload");
            this.addChild(obj.name, obj);

            obj = new TextArea("TextArea00","20","240","482","120",null,null,null,null,null,null,this);
            obj.set_taborder("4");
            this.addChild(obj.name, obj);
            // Layout Functions
            //-- Default Layout : this
            obj = new Layout("default","",1024,768,this,function(p){});
            obj.set_stepcount("0");
            this.addLayout(obj.name, obj);
            
            // BindItem Information

            
            // TriggerItem Information

        };
        
        this.loadPreloadList = function()
        {

        };
        
        // User Script
        this.registerScript("examFileUpTransfer.xfdl", function() {
        this.Button00_onclick = function(obj,e)
        {
        	console.log("-------------");
        	this.FileDialog00.open('Nexacro N', FileDialog.MULTILOAD);
        };

        this.FileDialog00_onclose = function(obj,e)
        {
        	this.addFileList(e.virtualfiles);
        };

        this.addFileList = function(filelist)
        {
        	for (var i = 0, len = filelist.length, vFile; i < len; i++)
        	{
        		vFile = filelist[i];
        		vFile.addEventHandler("onsuccess", this.FileList_onsuccess, this);
        		vFile.addEventHandler("onerror", this.FileList_onerror , this);

        		vFile.open(null, 1);
        	}
        }

        this.FileList_onsuccess = function(obj, e)
        {
        	switch (e.reason)
        	{
        		case 1:
        			obj.getFileSize();
        			break;
        		case 9:
        			var nRowIdx = this.Dataset00.addRow();
        			this.Dataset00.setColumn(nRowIdx, 0, obj.filename);
        			this.Dataset00.setColumn(nRowIdx, 1, this.cutFileSize(e.filesize));
        			this.FileUpTransfer00.addFile(obj.filename, obj);
        			break;
        	}
        }

        this.FileList_onerror = function(obj, e)
        {
        	trace("errortype: "+e.errortype);
        	trace("errormsg: "+e.errormsg);
        	trace("statuscode: "+e.statuscode);
        }

        // https://developer.mozilla.org/en-US/docs/Web/API/File/Using_files_from_web_applications#Example_Showing_file(s)_size
        this.cutFileSize = function(filesize)
        {
        	var sOutput = filesize + " bytes";
        	for (var aMultiples = ["KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"], nMultiple = 0, nApprox = filesize / 1024; nApprox > 1; nApprox /= 1024, nMultiple++)
        	{
        		sOutput = nApprox.toFixed(3) + " " + aMultiples[nMultiple];
        	}
        	return sOutput;
        };
        this.Grid00_ondragenter = function(obj,e)
        {
        	if(e.datatype == "file")
        	{
        		this.Grid00.opacity = 0.5;
        	}
        };

        this.Grid00_ondragleave = function(obj,e)
        {
        	this.Grid00.opacity = 1;
        };

        this.Grid00_ondrop = function(obj,e)
        {
        	this.Grid00.opacity = 1;
        	if(e.datatype == "file")
        	{
        		this.addFileList(e.filelist);
        	}
        };

        this.Button01_onclick = function(obj,e)
        {
        	this.TextArea00.value = "";
        	//
        	this.FileUpTransfer00.upload('http://localhost:8181/nexacro-web/fileupdown/fileupload.jsp');
        	//this.FileUpTransfer00.upload('http://demo.nexacro.com/developer_guide/17/Service/fileupload.jsp');
        };

        this.FileUpTransfer00_onprogress = function(obj,e)
        {
        	this.fn_addlog(e.loaded+"/"+e.total);
        };

        this.FileUpTransfer00_onsuccess = function(obj,e)
        {
        	this.fn_addlog(e.code);
        	this.fn_addlog(e.message);
        };

        this.FileUpTransfer00_onerror = function(obj,e)
        {
        	this.fn_addlog(e.errormsg);
        	this.fn_addlog(e.statuscode);
        };

        this.fn_addlog = function(strMessage)
        {
        	this.TextArea00.insertText(strMessage + '\n');

        }

        });
        
        // Regist UI Components Event
        this.on_initEvent = function()
        {
            this.Button00.addEventHandler("onclick",this.Button00_onclick,this);
            this.Grid00.addEventHandler("ondragenter",this.Grid00_ondragenter,this);
            this.Grid00.addEventHandler("ondragleave",this.Grid00_ondragleave,this);
            this.Grid00.addEventHandler("ondrop",this.Grid00_ondrop,this);
            this.Button01.addEventHandler("onclick",this.Button01_onclick,this);
            this.FileDialog00.addEventHandler("onclose",this.FileDialog00_onclose,this);
            this.FileUpTransfer00.addEventHandler("onprogress",this.FileUpTransfer00_onprogress,this);
            this.FileUpTransfer00.addEventHandler("onsuccess",this.FileUpTransfer00_onsuccess,this);
            this.FileUpTransfer00.addEventHandler("onerror",this.FileUpTransfer00_onerror,this);
        };
        this.loadIncludeScript("examFileUpTransfer.xfdl");
        this.loadPreloadList();
        
        // Remove Reference
        obj = null;
    };
}
)();

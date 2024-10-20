(function()
{
    return function()
    {
        if (!this._is_form)
            return;
        
        var obj = null;
        
        this.on_create = function()
        {
            this.set_name("Form_Work");
            this.set_titletext("Form_Work");
            if (Form == this.constructor)
            {
                this._setFormPosition(1280,670);
            }
            
            // Object(Dataset, ExcelExportObject) Initialize
            obj = new Dataset("dsExportExcelList", this);
            obj._setContents("<ColumnInfo><Column id=\"ID\" type=\"STRING\" size=\"256\"/><Column id=\"NAME\" type=\"STRING\" size=\"256\"/></ColumnInfo><Rows><Row><Col id=\"ID\">1</Col><Col id=\"NAME\">1</Col></Row><Row><Col id=\"ID\">2</Col><Col id=\"NAME\">2</Col></Row><Row><Col id=\"ID\">3</Col><Col id=\"NAME\">3</Col></Row></Rows>");
            this.addChild(obj.name, obj);
            
            // UI Components Initialize
            obj = new Grid("grdList01","62","85","1009","108",null,null,null,null,null,null,this);
            obj.set_taborder("0");
            obj.set_binddataset("dsExportExcelList");
            obj._setContents("<Formats><Format id=\"default\"><Columns><Column size=\"80\"/><Column size=\"80\"/></Columns><Rows><Row band=\"head\" size=\"24\"/><Row size=\"24\"/></Rows><Band id=\"head\"><Cell text=\"ID\"/><Cell col=\"1\" text=\"NAME\"/></Band><Band id=\"body\"><Cell text=\"bind:ID\"/><Cell col=\"1\" text=\"bind:NAME\"/></Band></Format></Formats>");
            this.addChild(obj.name, obj);

            obj = new Button("Button00","64.00","44","226","36",null,null,null,null,null,null,this);
            obj.set_taborder("1");
            obj.set_text("Button port 8181 exam-nexacro-XExportImport");
            this.addChild(obj.name, obj);

            obj = new TextArea("txtLog","64","220","491","168",null,null,null,null,null,null,this);
            obj.set_taborder("2");
            this.addChild(obj.name, obj);

            obj = new Button("Button01","311","45","155","36",null,null,null,null,null,null,this);
            obj.set_taborder("3");
            obj.set_text("testfile");
            this.addChild(obj.name, obj);

            obj = new Button("Button02","489","47","199","33",null,null,null,null,null,null,this);
            obj.set_taborder("4");
            obj.set_text("filetransuupdown");
            this.addChild(obj.name, obj);

            obj = new Button("Button03","598","235","208","81",null,null,null,null,null,null,this);
            obj.set_taborder("5");
            obj.set_text("E:\\ExtFileUpDownload");
            this.addChild(obj.name, obj);

            obj = new Button("Button04","64","7","627","30",null,null,null,null,null,null,this);
            obj.set_taborder("6");
            obj.set_text("session cluster1");
            this.addChild(obj.name, obj);
            // Layout Functions
            //-- Default Layout : this
            obj = new Layout("default","Desktop_screen",1280,670,this,function(p){});
            this.addLayout(obj.name, obj);
            
            // BindItem Information

            
            // TriggerItem Information

        };
        
        this.loadPreloadList = function()
        {

        };
        
        // User Script
        this.registerScript("Form_Work.xfdl", function() {

        //this.exportUrl = "http://localhost:8181/nexacro-xeni-java/XExportImport";
        //this.exportUrl = "/nexacro-xeni-java/XExportImport";
        this.exportUrl = "/XExportImport.do";
        //this.exportUrl = "http://localhost:8181/XExportImport";

        this.gfnCreateExportObject = function(pFileName){

        	//excel export object id
            var sExportId = "objExcelExport";

            //ExcelExportObject : invisible object
            var objExport = this.objects[sExportId];

            //already created ExcelExportObject
            if(!this.gfnIsNull(objExport))
            {
                return objExport;
            }

            objExport = new ExcelExportObject();

            //ExcelExportObject set property
            objExport.set_exporttype(nexacro.ExportTypes.EXCEL2007);
            objExport.set_exporturl(this.exportUrl);
            objExport.set_exportfilename(pFileName);

            //add event - onsuccess, onerror
            objExport.addEventHandler("onprogress", this.Export_onprogress, this);
            objExport.addEventHandler("onsuccess", this.Export_onsuccess, this);
            objExport.addEventHandler("onerror", this.Export_onerror, this);

            return objExport;
        }

        this.gfnBasicExport = function(pFileName)
        {
            var sFileName = "ExportData(BasicExport)";
            if(!this.gfnIsNull(pFileName))
            {
                sFileName = pFileName;
            }

            //ExcelExportObject
            var objExport = this.gfnCreateExportObject(sFileName);

            //export Grid = export item source
            var objGrid1 = this.grdList01;
            //var objGrid2 = this.grdList02;

            //export item ranges : sheet1 - grid1, grid2
            var sExportCommand1 = "Sheet1!A1";
            //var sExportCommand2 = "Sheet1!A12";

            //addExportItem
            objExport.addExportItem(nexacro.ExportItemTypes.GRID, objGrid1, sExportCommand1);
            //objExport.addExportItem(nexacro.ExportItemTypes.GRID, objGrid2, sExportCommand2);
            objExport.exportData();
        };

        this.Export_onsuccess = function (obj, e)
        {
            this.txtLog.set_value(this.txtLog.value + e.eventid +  "  Export_onsuccess\n");
        };

        /**
        * @description ExcelExportObject onerror event
        */
        this.Export_onerror = function (obj, e)
        {
            this.txtLog.set_value(this.txtLog.value + e.eventid +  "  Export_onerror\n");
        };

        this.Export_onprogress = function(obj,e)
        {
            this.txtLog.set_value(this.txtLog.value
                                  + e.eventid +  "  Export_onprogress : "
                                  + "recordindex " + e.recordindex + "\n");
        };



        /**************************************************************************
        *  공통 함수 처리 영역
        해당 함수의 경우 프로젝트 사용 시 프로젝트 공통함수로 전환을 권장 드립니다.
        **************************************************************************/
        /**
        * Function Name : gfnIsNull
        * Description   : 입력값이 null에 해당하는 경우 모두를 한번에 체크한다.
        * Arguments     : sValue - 체크할 문자열( 예 : null 또는 undefined 또는 "" 또는 "abc" )
        * Return        : Boolean sValue이 undefined, null, NaN, "", Array.length = 0인 경우 true
        */
        this.gfnIsNull = function (sValue)
        {
            if (new String(sValue).valueOf() == "undefined")
            {
                return true;
            }

            if (sValue == null)
            {
                return true;
            }

            var v_ChkStr = new String(sValue);

            if (v_ChkStr == null)
            {
                return true;
            }

            if (v_ChkStr.toString().length == 0)
            {
                return true;
            }

            return false;
        };




        this.Button00_onclick = function(obj,e)
        {
        	console.log("excel object 추가 -----------------------------------");

        	this.gfnBasicExport();
        };


        this.Button01_onclick = function(obj,e)
        {
        	this.go("FrameBase::examFileUpTransfer.xfdl");
        };

        this.Button02_onclick = function(obj,e)
        {
        	this.go("FrameBase::filetransupdown.xfdl");
        };

        this.Button04_onclick = function(obj,e)
        {
        	console.log("---------");
        };

        });
        
        // Regist UI Components Event
        this.on_initEvent = function()
        {
            this.Button00.addEventHandler("onclick",this.Button00_onclick,this);
            this.Button01.addEventHandler("onclick",this.Button01_onclick,this);
            this.Button02.addEventHandler("onclick",this.Button02_onclick,this);
            this.Button04.addEventHandler("onclick",this.Button04_onclick,this);
        };
        this.loadIncludeScript("Form_Work.xfdl");
        this.loadPreloadList();
        
        // Remove Reference
        obj = null;
    };
}
)();

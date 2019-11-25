package com.usc.app.action.editor.codegor;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.obj.api.USCObject;
import com.usc.obj.api.impl.ApplicationContext;
import com.usc.server.DBConnecter;
import com.usc.server.util.StringHelperUtil;
import com.usc.util.ObjectHelperUtils;

import lombok.Data;

@Data
public class CodeGenerator
{

	private String resultCode;
	private ApplicationContext context;
	private Boolean exhaust = true;

	public CodeGenerator(ApplicationContext context)
	{
		this.exhaust = true;
		this.context = context;
	}

	public synchronized void generator(USCObject[] objects)
	{
		this.resultCode = getCode(objects);
		if (this.exhaust)
		{
			int i = 0;
			while (this.resultCode == null)
			{
				i++;
				this.resultCode = getCode(objects);
				if (i == 2000)
				{
					return;
				}
			}
		}
	}

	private synchronized String getCode(USCObject[] objects)
	{
		String plCodeID = objects[objects.length - 1].getID();
		while (!CodeLock.locked(plCodeID))
		{
			CodeLock.putCodeID(plCodeID);
			StringBuffer buffer = new StringBuffer();
			for (USCObject uscObject : objects)
			{
				int type = (int) uscObject.getFieldValue("dataType");

				if (type == 1)
				{
					String prefix = uscObject.getFieldValueToString("prefix");
					buffer.append(prefix);
					Object connector = uscObject.getFieldValue("connector");
					if (!ObjectHelperUtils.isEmpty(connector))
					{
						buffer.append(connector);
					}
				} else if (type == 2)
				{
					Generator generator = new Generator(uscObject);
					generator.run();
					this.exhaust = generator.exhaust;
					if (exhaust)
					{
						buffer.append(generator.getMaxValue());
						String resultCode = buffer.toString();
						if (resultCode != null)
						{
							CodeLock.removeCodeID(plCodeID);
							return resultCode;
						}
					} else
					{
						return null;
					}
				}
			}

		}
		return null;

	}

	@Data
	private class Generator implements Runnable
	{
		private boolean exhaust;

		private String startCode;
		private String endtCode;
		private String maxCode;
		private int length;
		private USCObject object;

		private String maxValue;

		public Generator(USCObject uscObject)
		{
			this.exhaust = true;
			this.object = uscObject;
			init(uscObject);

		}

		private synchronized void init(USCObject uscObject)
		{

			this.startCode = object.getFieldValueToString("startcode");
			this.endtCode = object.getFieldValueToString("endcode");
			this.length = (int) object.getFieldValue("code_segment");

		}

		@Override
		public void run()
		{
			synchronized (this)
			{
				String id = this.object.getID();
				Object maxC = null;
				try
				{
					Map<String, Object> codeData = new JdbcTemplate(DBConnecter.getDataSource())
							.queryForMap("select maxcode from sys_codestandard where id='" + id + "' for update");
					maxC = codeData.get("maxcode");
				} catch (Exception e)
				{
					e.printStackTrace();
				}

				if (!ObjectHelperUtils.isEmpty(maxC))
				{
					if (endtCode.equals(String.valueOf(maxC)))
					{
						this.exhaust = false;
						return;
					}
					this.maxCode = (String) maxC;
					int _max = Integer.valueOf(this.maxCode);
					this.maxValue = StringHelperUtil.addCodeFormat(_max, this.length);
				} else
				{
					this.maxValue = StringHelperUtil.formatString(this.startCode, this.length);
				}
				this.object.setFieldValue("maxcode", this.maxValue);
				this.object.save(context);
			}

		}

	}

}

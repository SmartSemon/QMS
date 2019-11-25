package com.usc.obj.api.type.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.obj.api.InvokeContext;
import com.usc.obj.api.bean.UserInformation;
import com.usc.obj.api.type.AbstractDBObj;
import com.usc.server.DBConnecter;
import com.usc.util.ObjectHelperUtils;

public class UserInfoObject extends AbstractDBObj implements EmployeeInfo, Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6968093484780401270L;
	protected UserInformation information;

	public UserInfoObject(String objType, HashMap<String, Object> map)
	{
		super("SUSER", map);
		infoIsNullSet();
	}

	public UserInfoObject(String objType, UserInformation info)
	{
		super("SUSER", info.toSystemUserMap());
		setInfoMation(info);
	}

	public UserInformation getInfomation()
	{
		return this.information;
	}

	public void setInfoMation(UserInformation info)
	{
		this.information = info;
	}

	public String getUserID()
	{
		return this.getID();
	}

	public String getUserName()
	{
		return this.getFieldValueToString("SNAME");
	}

	public String getOnDuty()
	{
		return this.getFieldValueToString("ONDUTY");
	}

	public String getUserAvatar()
	{
		return this.getFieldValueToString("avatar");
	}

	public String getUserSign()
	{
		return this.getFieldValueToString("sign");
	}

	public String getUserStatus()
	{
		return this.getFieldValueToString("status");
	}

	@Override
	public boolean canClone(InvokeContext context) throws Exception
	{
		return false;
	}

	@Override
	public String getEmployeeID()
	{
		return this.information.getEmployeeID();
	}

	@Override
	public String getEmployeeNo()
	{
		return this.information.getEmployeeNo();
	}

	@Override
	public String getEmployeeName()
	{
		return this.information.getEmployeeName();
	}

	@Override
	public String getEmployeeAge()
	{
		return this.information.getEmployeeRemark();
	}

	@Override
	public String getEmployeeSax()
	{
		return this.information.getSax();
	}

	@Override
	public String getEmployeeTel()
	{
		return this.information.getTel();
	}

	@Override
	public String getEmployeeEMail()
	{
		return this.information.getMail();
	}

	@Override
	public String getEmployeeIDCard()
	{
		return this.information.getIdCard();
	}

	@Override
	public String getEmployeeWkSate()
	{
		return this.information.getWkSate();
	}

	@Override
	public String getEmployeeDepartMentNo()
	{
		return this.information.getDepartMentNo();
	}

	@Override
	public String getEmployeeDepartMentName()
	{
		return this.information.getDepartMentName();
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	private void infoIsNullSet()
	{
		String sql = "SELECT U.id AS userID,U.onduty AS onDuty,U.name AS userName,U.remark AS userRemark,P.id AS employeeID,"
				+ "P.no AS employeeNo,P.name AS employeeName,P.sax AS sax,P.tel AS tel,P.mail AS mail,P.remark AS employeeRemark,P.age AS age,"
				+ "P.idcard AS idCard,P.wk_state AS wkSate,D.no AS departMentNo,D.name AS departMentName "
				+ "FROM suser U,spersonnel P,sdepartment D WHERE U.del=0 AND p.del=0 AND D.del=0 "
				+ "AND EXISTS(SELECT 1 FROM suser_relobj A,crl_spersonnel_obj B WHERE A.del=0 AND B.del=0  "
				+ "AND A.itembid=U.id AND A.itemaid=b.itemid AND B.itemid=P.id AND B.nodeid=D.id) AND name='"
				+ this.getUserName() + "'";
		try
		{

			List<UserInformation> list = (new JdbcTemplate(DBConnecter.getDataSource())).query(sql,
					new BeanPropertyRowMapper(UserInformation.class));
			if (!ObjectHelperUtils.isEmpty(list))
			{
				setInfoMation(list.get(0));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}

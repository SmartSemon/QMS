package com.usc.app.action.mate;

import java.util.List;
import java.util.Map;

import com.usc.app.action.a.AbstractAction;
import com.usc.app.util.tran.StandardResultTranslate;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.ItemRelationPage;
import com.usc.server.md.ItemRelationPageSign;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.USCModelMate;
import com.usc.util.ObjectHelperUtils;

public class GainItemRelationPageAction extends AbstractAction
{

	@Override
	public Object executeAction() throws Exception
	{
		ItemInfo info = context.getItemInfo();
		String mNo = context.getModelNo();
		ItemRelationPage relationPage = null;
		if (mNo == null)
		{
			relationPage = info.getDefaultItemRelationPage();
		} else
		{
			relationPage = info.getItemRelationPage(mNo);
		}
		if (relationPage != null)
		{
			getRelationModleData(info, relationPage);
			relationPage.setItemRelationPageSignMap(null);
			Map<String, Object> result = StandardResultTranslate.successfulOperation();
			result.put("id", relationPage.getId());
			result.put("name", relationPage.getName());
			result.put("itemRelationPage", relationPage);
			return result;
		}
		return failedOperation();
	}

	@Override
	public boolean disable() throws Exception
	{
		return false;
	}

	private void getRelationModleData(ItemInfo info, ItemRelationPage itemRelationPage)
	{
		if (itemRelationPage == null)
		{
			return;
		}
		List<ItemRelationPageSign> itemRelationPageSigns = itemRelationPage.getItemRelationPageSignList();
		if (ObjectHelperUtils.isEmpty(itemRelationPageSigns))
		{
			return;
		}
		for (ItemRelationPageSign itemRelationPageSign : itemRelationPageSigns)
		{
			String rType = itemRelationPageSign.getRType();
			switch (rType)
			{
			case "relationproperty":
				String propertyNo = itemRelationPageSign.getRelevanceNo();
				if (propertyNo != null)
				{
					itemRelationPageSign.setItemRelationPropertyPage(info.getItemPage(propertyNo));
				} else
				{
					itemRelationPageSign.setItemRelationPropertyPage(info.getDefaultItemPage());
				}
				break;

			case "relationpage":
				String relationShipNo = itemRelationPageSign.getRelevanceNo();
				ModelRelationShip modelRelationShipData = null;
				if (relationShipNo != null)
				{
					modelRelationShipData = USCModelMate.getRelationShipInfo(relationShipNo);
					ItemInfo infoBInfo = USCModelMate.getItemInfo(modelRelationShipData.getItemB());
					modelRelationShipData.setItemMenus(infoBInfo.getItemMenuList());
					modelRelationShipData.setItemGrid(infoBInfo.getDefaultItemGrid());
					modelRelationShipData.setItemPropertyPage(infoBInfo.getDefaultItemPage());

				}
				itemRelationPageSign.setModelRelationShip(modelRelationShipData);
				break;

			case "relationqueryview":

				break;

			default:
				break;
			}
		}

	}

}

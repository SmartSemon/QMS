package com.usc.server.init;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.usc.app.action.mate.MateFactory;
import com.usc.cache.redis.RedisUtil;
import com.usc.server.DBConnecter;
import com.usc.server.md.ItemInfo;
import com.usc.server.md.MenuLibrary;
import com.usc.server.md.ModelClassView;
import com.usc.server.md.ModelQueryView;
import com.usc.server.md.ModelRelationShip;
import com.usc.server.md.mapper.ItemRowMapper;
import com.usc.server.md.mapper.MenuLibraryRowMapper;
import com.usc.server.md.mapper.ModelClassViewRowMapper;
import com.usc.server.md.mapper.ModelQueryViewRowMapper;
import com.usc.server.md.mapper.RelationShipRowMapper;
import com.usc.util.ObjectHelperUtils;

public class InitModelData
{
	static RedisUtil redisUtil = new RedisUtil();
	private static final String MODELITEMSQL = "SELECT * FROM usc_model_item WHERE del=0 AND (effective IN(-1,1) AND state='F') OR (effective=1 AND state='HS')";
	private static final String MODELSHIPSQL = "SELECT * FROM usc_model_relationship WHERE del=0 AND (effective IN(-1,1) AND state='F') OR (effective=1 AND state='HS')";
	private static final String MODELQUERYVIEWSQL = "SELECT * FROM usc_model_queryview WHERE del=0 AND (effective IN(-1,1) AND state='F') OR (effective=1 AND state='HS')";
	private static final String MODELCLASSVIEWSQL = "SELECT * FROM usc_model_classview WHERE del=0 AND (effective IN(-1,1) AND state='F') OR (effective=1 AND state='HS')";
	private static final String MODELMENULIBRARYSQL = "SELECT * FROM usc_model_menu WHERE del=0 AND (effective IN(-1,1) AND state='F') OR (effective=1 AND state='HS')";

	private static final String MODEL_ITEMDATA = "MODEL_ITEMDATA";
	private static final String MODEL_ITEMDATABYTABLE = "MODEL_ITEMDATABYTABLE";
	private static final String MODEL_RELATIONSHIPDATA = "MODEL_RELATIONSHIPDATA";
	private static final String MODEL_QUERYVIEWDATA = "MODEL_QUERYVIEWDATA";
	private static final String MODEL_CLASSVIEWDATA = "MODEL_CLASSVIEWDATA";
	private static final String MODEL_MENULIBRARY = "MODEL_MENULIBRARY";

	public static void initModel()
	{
		JdbcTemplate jdbcTemplate = DBConnecter.getJdbcTemplate();
		List<ItemInfo> itemInfos = jdbcTemplate.query(MODELITEMSQL, new ItemRowMapper());
		if (!ObjectHelperUtils.isEmpty(itemInfos))
		{
			redisUtil.del(MODEL_ITEMDATA);
			MateFactory.clearItemInfo();
			itemInfos.forEach((itemInfo) -> {
				redisUtil.hset(MODEL_ITEMDATA, itemInfo.getItemNo(), itemInfo);
			});

			itemInfos.forEach((itemInfo) -> redisUtil.hset(MODEL_ITEMDATABYTABLE, itemInfo.getTableName(), itemInfo));
		}

		List<ModelRelationShip> relationShipInfos = jdbcTemplate.query(MODELSHIPSQL, new RelationShipRowMapper());
		if (!ObjectHelperUtils.isEmpty(relationShipInfos))
		{
			MateFactory.clearRelationShip();
			redisUtil.del(MODEL_RELATIONSHIPDATA);
			relationShipInfos.forEach((modelRelationShip) -> redisUtil.hset(MODEL_RELATIONSHIPDATA,
					modelRelationShip.getNo(), modelRelationShip));
		}

		List<ModelQueryView> modelQueryViews = jdbcTemplate.query(MODELQUERYVIEWSQL, new ModelQueryViewRowMapper());
		if (!ObjectHelperUtils.isEmpty(modelQueryViews))
		{
			MateFactory.clearQueryView();
			redisUtil.del(MODEL_QUERYVIEWDATA);
			modelQueryViews.forEach(
					(modelQueryView) -> redisUtil.hset(MODEL_QUERYVIEWDATA, modelQueryView.getNo(), modelQueryView));
		}
		List<ModelClassView> modelClassViews = jdbcTemplate.query(MODELCLASSVIEWSQL, new ModelClassViewRowMapper());
		if (!ObjectHelperUtils.isEmpty(modelClassViews))
		{
			MateFactory.clearClassView();
			redisUtil.del(MODEL_CLASSVIEWDATA);
			modelClassViews.forEach(
					(modelClassView) -> redisUtil.hset(MODEL_CLASSVIEWDATA, modelClassView.getNo(), modelClassView));
		}

		List<MenuLibrary> menuLibrary = jdbcTemplate.query(MODELMENULIBRARYSQL, new MenuLibraryRowMapper());
		if (!ObjectHelperUtils.isEmpty(menuLibrary))
		{
			redisUtil.del(MODEL_MENULIBRARY);
			menuLibrary.forEach((menu) -> redisUtil.hset(MODEL_MENULIBRARY, menu.getImplclass(), menu));
		}
	}

}

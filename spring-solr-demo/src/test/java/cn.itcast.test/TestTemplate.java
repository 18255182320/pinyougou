package cn.itcast.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.pojo.TbItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class TestTemplate {
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	
	@Test
	public void testAdd() {
		TbItem item=new TbItem();
		item.setId(1L);
		item.setBrand("��Ϊ");
		item.setCategory("�ֻ�");
		item.setGoodsId(1L);
		item.setSeller("��Ϊ2��ר����");
		item.setTitle("��ΪMate9");
		item.setPrice(new BigDecimal(2000));
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}
	
	@Test
	public void testFindOne(){
		TbItem item = solrTemplate.getById(1, TbItem.class);
		System.out.println(item.getTitle());
	}
	
	@Test
	public void testDelete(){
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	
	@Test
	public void testAddList(){
		List<TbItem> list=new ArrayList();
		
		for(int i=0;i<100;i++){
			TbItem item=new TbItem();
			item.setId(i+1L);
			item.setBrand("��Ϊ");
			item.setCategory("�ֻ�");
			item.setGoodsId(1L);
			item.setSeller("��Ϊ2��ר����");
			item.setTitle("��ΪMate"+i);
			item.setPrice(new BigDecimal(2000+i));	
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Test
	public void testPageQuery(){
		Query query=new SimpleQuery("*:*");
		query.setOffset(20);//��ʼ������Ĭ��0��
		query.setRows(20);//ÿҳ��¼��(Ĭ��10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("�ܼ�¼����"+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}	
	//��ʾ��¼����
	private void showList(List<TbItem> list){		
		for(TbItem item:list){
			System.out.println(item.getTitle() +item.getPrice());
		}		
	}

	
	@Test
	public void testPageQueryMutil(){	
		Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_category").contains("�ֻ�");
		criteria=criteria.and("item_title").contains("5");		
		query.addCriteria(criteria);
		//query.setOffset(20);//��ʼ������Ĭ��0��
		//query.setRows(20);//ÿҳ��¼��(Ĭ��10)
		ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
		System.out.println("�ܼ�¼����"+page.getTotalElements());
		List<TbItem> list = page.getContent();
		showList(list);
	}
	
	@Test
	public void testDeleteAll(){
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
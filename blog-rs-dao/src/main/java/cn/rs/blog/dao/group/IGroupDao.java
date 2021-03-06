package cn.rs.blog.dao.group;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.rs.blog.bean.group.Group;
import cn.rs.blog.dao.common.IBaseDao;

/**
 * Created by rs
 */
public interface IGroupDao extends IBaseDao<Group> {

    /**
     * 获取群组
     * @return
     */
    List<Group> list(@Param("status") Integer status, @Param("key") String key);

    /**
     * 修改状态
     *
     * @param id
     * @return
     */
    Integer changeStatus(@Param("id") Integer id);

    List<Group> listByCustom(@Param("status") int status, @Param("num") int num, @Param("sort") String sort);

}
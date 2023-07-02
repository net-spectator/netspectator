package services;

import entities.EquipmentType;
import entities.TrackedEquipment;
import enums.Status;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import utils.NSLogger;
import utils.converter.CastUtils;


import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.lang.reflect.Executable;
import java.util.List;

@Transactional
public class DataBaseService {
    private static final NSLogger LOGGER = new NSLogger(DataBaseService.class);


    private static final SessionFactory factory;
    private static Session session = null;

    static {
        factory = new Configuration() // TODO: 23.06.2023 обеспечить доступ к этому классу из других классов
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    //возвращает полный список устройств со статусами
    public static String[] getTrackedEquipmentList() {

        return null;
    }

    //добавляет новое устройство в базу данных
    public static synchronized int addTrackedEquipment(TrackedEquipment device) {
        session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(device);
        } catch (Exception e) {
            return -1;
        }
        session.getTransaction().commit();
        return 1;
    }

    public static synchronized TrackedEquipment getTrackedEquipmentByUUID(String uuid) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        TrackedEquipment device = null;
        try {
            device = (TrackedEquipment) session.createQuery("from TrackedEquipment where equipmentUuid=:uuid").setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return null;
        }
        session.getTransaction().commit();
        return device;
    }

    public static synchronized void changeDeviceStatus(String uuid, boolean status) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        TrackedEquipment device = null;
        try {
            device = (TrackedEquipment) session.createQuery("from TrackedEquipment where equipmentUuid=:uuid").setParameter("uuid", uuid).getSingleResult();
            device.setEquipmentOnlineStatus(status ? 1 : 0);
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return;
        }
        session.getTransaction().commit();
    }

    public static synchronized void updateTrackedEquipment(TrackedEquipment device) {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            session.update(device);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            LOGGER.error(String.format("Проблемы при обновлении сущности %s", device.getEquipmentTitle()));
        }
    }

    //удаляет выбранное устройство из базы данных
    public static int deleteTrackedEquipment(TrackedEquipment device) {
        try {
            session = factory.getCurrentSession();
            session.beginTransaction();
            session.delete(device);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            LOGGER.error(String.format("Проблемы при удалении сущности %s", device.getEquipmentTitle()));
        }
        return -1;
    }

    public static synchronized TrackedEquipment getTrackedNodeByIP(String ipAddress) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        TrackedEquipment te = null;
        try {
            te = (TrackedEquipment) session.createQuery("from TrackedEquipment where equipmentIpAddress=:ip").setParameter("ip", ipAddress).getSingleResult();
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return null;
        }
        session.getTransaction().commit();
        return te;
    }

    public static synchronized List<TrackedEquipment> getTrackedNodesListByType(String typeName) {
        session = factory.getCurrentSession();
        session.beginTransaction();
        List<TrackedEquipment> te = null;
        EquipmentType equipmentType = null;
        try {
            equipmentType =  (EquipmentType) session.createQuery("from EquipmentType where typeTitle=:title").setParameter("title", typeName).getSingleResult();
        } catch (NoResultException e) {
            session.getTransaction().commit();
            return null;
        }
        session.getTransaction().commit();
        return equipmentType.getTrackedEquipmentList();
    }

}

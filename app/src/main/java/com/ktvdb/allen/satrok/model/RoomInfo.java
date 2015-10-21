package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/8/27.
 */
public class RoomInfo
{

    /**
     * id : 293605ea-7c53-11e4-9b3f-52540054ca8d
     * code : B122
     * typeCode : 09
     * description : 北斗02
     * persons :
     * areaId : c2abc30078b94a4b9ff16a88487055c6
     * skinId : 123456789
     * onDemandModelId : 000000004ce0b12d014ce0e4266a0001
     * status : 1
     * businessStatus : 1
     * sessionId : 5f25253e-424b-82de-757c-81fa037072f3
     * checkOutTime : 1430234651000
     * lastUpdateTime : 1437030876000
     */

    private String id;
    private String code;
    private String typeCode;
    private String description;
    private String persons;
    private String areaId;
    private String skinId;
    private String onDemandModelId;
    private int    status;
    private int    businessStatus;
    private String sessionId;
    private long   checkOutTime;
    private long   lastUpdateTime;

    public void setId(String id) { this.id = id;}

    public void setCode(String code) { this.code = code;}

    public void setTypeCode(String typeCode) { this.typeCode = typeCode;}

    public void setDescription(String description) { this.description = description;}

    public void setPersons(String persons) { this.persons = persons;}

    public void setAreaId(String areaId) { this.areaId = areaId;}

    public void setSkinId(String skinId) { this.skinId = skinId;}

    public void setOnDemandModelId(String onDemandModelId) { this.onDemandModelId = onDemandModelId;}

    public void setStatus(int status) { this.status = status;}

    public void setBusinessStatus(int businessStatus) { this.businessStatus = businessStatus;}

    public void setSessionId(String sessionId) { this.sessionId = sessionId;}

    public void setCheckOutTime(long checkOutTime) { this.checkOutTime = checkOutTime;}

    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime;}

    public String getId() { return id;}

    public String getCode() { return code;}

    public String getTypeCode() { return typeCode;}

    public String getDescription() { return description;}

    public String getPersons() { return persons;}

    public String getAreaId() { return areaId;}

    public String getSkinId() { return skinId;}

    public String getOnDemandModelId() { return onDemandModelId;}

    public int getStatus() { return status;}

    public int getBusinessStatus() { return businessStatus;}

    public String getSessionId() { return sessionId;}

    public long getCheckOutTime() { return checkOutTime;}

    public long getLastUpdateTime() { return lastUpdateTime;}
}

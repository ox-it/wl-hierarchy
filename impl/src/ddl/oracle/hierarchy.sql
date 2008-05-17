
    create table HIERARCHY_NODE (
        ID number(19,0) not null,
        directParentIds varchar2(2000),
        parentIds varchar2(4000),
        directChildIds varchar2(2000),
        childIds varchar2(4000),
        primary key (ID)
    );

    create table HIERARCHY_NODE_META (
        ID number(19,0) not null,
        hierarchyId varchar2(255),
        isRootNode number(1,0) not null,
        ownerId varchar2(255),
        title varchar2(255),
        description clob,
        permToken varchar2(255),
        primary key (ID)
    );

    create index HIERARCHY_HID on HIERARCHY_NODE_META (hierarchyId);

    create index HIERARCHY_PERMTOKEN on HIERARCHY_NODE_META (permToken);

    create sequence HIERARCHY_META_ID_SEQ;

    create sequence HIERARCHY_NODE_ID_SEQ;

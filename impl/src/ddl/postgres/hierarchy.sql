
    create table HIERARCHY_NODE (
        ID int8 not null,
        directParentIds varchar(2000),
        parentIds varchar(4000),
        directChildIds varchar(2000),
        childIds varchar(4000),
        primary key (ID)
    );

    create table HIERARCHY_NODE_META (
        ID int8 not null,
        hierarchyId varchar(255),
        isRootNode bool not null,
        ownerId varchar(255),
        title varchar(255),
        description text,
        permToken varchar(255),
        primary key (ID)
    );

    create index HIERARCHY_HID on HIERARCHY_NODE_META (hierarchyId);

    create index HIERARCHY_PERMTOKEN on HIERARCHY_NODE_META (permToken);

    create sequence HIERARCHY_META_ID_SEQ;

    create sequence HIERARCHY_NODE_ID_SEQ;


    create table HIERARCHY_NODE (
        ID numeric(19,0) identity not null,
        directParentIds varchar(2000) null,
        parentIds varchar(4000) null,
        directChildIds varchar(2000) null,
        childIds varchar(4000) null,
        primary key (ID)
    );

    create table HIERARCHY_NODE_META (
        ID numeric(19,0) identity not null,
        hierarchyId varchar(255) null,
        isRootNode tinyint not null,
        ownerId varchar(255) null,
        title varchar(255) null,
        description text null,
        permToken varchar(255) null,
        primary key (ID)
    );

    create index HIERARCHY_HID on HIERARCHY_NODE_META (hierarchyId);

    create index HIERARCHY_PERMTOKEN on HIERARCHY_NODE_META (permToken);

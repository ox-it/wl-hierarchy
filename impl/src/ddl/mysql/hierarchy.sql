
    create table HIERARCHY_NODE (
        ID bigint not null auto_increment,
        directParentIds text,
        parentIds text,
        directChildIds text,
        childIds text,
        primary key (ID)
    );

    create table HIERARCHY_NODE_META (
        ID bigint not null auto_increment,
        hierarchyId varchar(255),
        isRootNode bit not null,
        ownerId varchar(255),
        title varchar(255),
        description text,
        permToken varchar(255),
        primary key (ID)
    );

    create index HIERARCHY_HID on HIERARCHY_NODE_META (hierarchyId);

    create index HIERARCHY_PERMTOKEN on HIERARCHY_NODE_META (permToken);

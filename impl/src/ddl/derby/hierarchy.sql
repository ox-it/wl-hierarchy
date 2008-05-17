
    create table HIERARCHY_NODE (
        ID bigint not null,
        directParentIds varchar(2000),
        parentIds varchar(4000),
        directChildIds varchar(2000),
        childIds varchar(4000),
        primary key (ID)
    );

    create table HIERARCHY_NODE_META (
        ID bigint not null,
        hierarchyId varchar(255),
        isRootNode smallint not null,
        ownerId varchar(255),
        title varchar(255),
        description clob(255),
        permToken varchar(255),
        primary key (ID)
    );

    create index HIERARCHY_HID on HIERARCHY_NODE_META (hierarchyId);

    create index HIERARCHY_PERMTOKEN on HIERARCHY_NODE_META (permToken);

    create table hibernate_unique_key (
         next_hi integer 
    );

    insert into hibernate_unique_key values ( 0 );

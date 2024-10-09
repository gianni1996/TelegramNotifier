package com.trello_talk.trello_talk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workspace")
public class Workspace {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "description")
    private String description;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "id_enterprise")
    private String idEnterprise;

    @Column(name = "id_entitlement")
    private String idEntitlement;

    @Column(name = "id_member_creator")
    private String idMemberCreator;

    @Column(name = "invited")
    private Boolean invited;

    @Column(name = "members_count")
    private Integer membersCount;

    @Column(name = "node_id")
    private String nodeId;

    @Column(name = "offering")
    private String offering;

    @Column(name = "billable_member_count")
    private Integer billableMemberCount;

    @Column(name = "billable_collaborator_count")
    private Integer billableCollaboratorCount;

    @Column(name = "url")
    private String url;

    @Column(name = "website")
    private String website;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "standard_variation")
    private String standardVariation;

    @Column(name = "available_license_count")
    private Integer availableLicenseCount;

    @Column(name = "maximum_license_count")
    private Integer maximumLicenseCount;

    @Column(name = "ix_update")
    private String ixUpdate;

    @Column(name = "team_type")
    private String teamType;

    @Column(name = "date_last_activity")
    private Calendar dateLastActivity;

    @Column(name = "jwm_link")
    private String jwmLink;

    @Column(name = "active_membership_count")
    private Integer activeMembershipCount;

    @Column(name = "type")
    private String type;

    @Column(name = "reverse_trial_tag")
    private String reverseTrialTag;
}

CREATE TABLE DHI_MX_AUX_PAGOS_CNF_GBL(
    id_pagos_gbl NUMBER(10),
    val_tp_pago  NUMBER(4),
    id_reg_api VARCHAR2(18),
    val_cnl        VARCHAR2(3),
    cod_emp        VARCHAR2(4),
    cod_pto_vta    VARCHAR2(4),
    val_med_ent    VARCHAR2(6),
    val_ref        VARCHAR2(7),
    cod_usu_cap    VARCHAR2(10),
    cod_usu_sol    VARCHAR2(10),
    cod_cve_tra    VARCHAR2(5),
    cod_cve_oper   VARCHAR2(8),
    imp            NUMBER(13,2),
    txt_com        VARCHAR2(120),
    val_div_ord    VARCHAR2(4),
    val_cta_ord    VARCHAR2(35),
    txt_nom_ord    VARCHAR2(140),
    val_ip         VARCHAR2(15),
    val_buc_li     VARCHAR2(20),
    val_cta_rec    VARCHAR2(35),
    txt_nom_rec    VARCHAR2(140),
    val_rfc_ben    VARCHAR2(18),
    id_int         VARCHAR2(35), ---
    txt_nomb_int   VARCHAR2(70),
    txt_ciud_int   VARCHAR2(30),
    cod_cpais_int  VARCHAR2(4),
    val_bic_int    VARCHAR2(11),
    cod_cpais_rec  VARCHAR2(4),
    val_bic_rec    VARCHAR2(11),
    id_ban_rec     VARCHAR2(35), --
    txt_nom_ins_rec  VARCHAR2(70),
    txt_cuid_ins_rec VARCHAR2(30), ---
    val_cta_bco_rec  VARCHAR2(35),--
    val_num_cta_rec  VARCHAR2(35),---
    val_tip_cta_rec  VARCHAR2(3),
    txt_nomb_rec   VARCHAR2(140),
    txt_calle_ben_rec VARCHAR2(70),
    val_numb_ben_rec  VARCHAR2(7),
    val_cp_ben_rec  VARCHAR2(16),---
    txt_dir_ben_rec VARCHAR2(70),
    cod_cve_div_rec VARCHAR2(4),---
    imp_abono   NUMBER(14,2),
    txt_ciud_rec  VARCHAR2(35),
    cod_pais_rec  VARCHAR2(4),
    txt_inf_rem   VARCHAR2(140),
    id_can_cam    VARCHAR2(2),---
    cod_cve_des   VARCHAR2(10),
    val_tipo_ben_rec  VARCHAR2(1),--
    val_prop_trans VARCHAR2(3),--
    fch_reg           date,
    val_estat     VARCHAR2(4)


) 
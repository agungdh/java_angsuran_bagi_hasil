/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.test.Models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author user
 */
@Table("angsuran")
@IdName("id_angsuran")
@BelongsTo(parent = PembiayaanModel.class, foreignKeyName = "id_pembiayaan")
public class AngsuranModel extends Model {}
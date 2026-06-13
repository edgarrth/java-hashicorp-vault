package com.edgar.vaultpoc.infrastructure.db;
import com.edgar.vaultpoc.domain.model.*;import com.edgar.vaultpoc.domain.ports.*;import org.springframework.stereotype.Repository;
import java.sql.*;import java.util.*;
@Repository
public class JdbcPaymentRepositoryAdapter implements PaymentRepositoryPort{
 private final SecretManagerPort vault; private final AppProperties props;
 public JdbcPaymentRepositoryAdapter(SecretManagerPort vault, AppProperties props){this.vault=vault;this.props=props;}
 private Connection connection() throws SQLException{var c=vault.dynamicDatabaseCredentials();return DriverManager.getConnection(props.jdbcBaseUrl(),c.username(),c.password());}
 public Payment save(Payment p){String sql="insert into payments(id,merchant_id,amount,currency,masked_card,encrypted_card,status,created_at) values(?,?,?,?,?,?,?,?)";try(Connection cn=connection();PreparedStatement ps=cn.prepareStatement(sql)){ps.setObject(1,p.id());ps.setString(2,p.merchantId());ps.setBigDecimal(3,p.amount());ps.setString(4,p.currency());ps.setString(5,p.maskedCard());ps.setString(6,p.encryptedCard());ps.setString(7,p.status().name());ps.setTimestamp(8,Timestamp.from(p.createdAt()));ps.executeUpdate();return p;}catch(SQLException e){throw new IllegalStateException("db save failed using Vault dynamic credentials",e);}}
 public Optional<Payment> findById(UUID id){String sql="select * from payments where id=?";try(Connection cn=connection();PreparedStatement ps=cn.prepareStatement(sql)){ps.setObject(1,id);ResultSet rs=ps.executeQuery();if(!rs.next()) return Optional.empty();return Optional.of(new Payment((UUID)rs.getObject("id"),rs.getString("merchant_id"),rs.getBigDecimal("amount"),rs.getString("currency"),rs.getString("masked_card"),rs.getString("encrypted_card"),PaymentStatus.valueOf(rs.getString("status")),rs.getTimestamp("created_at").toInstant()));}catch(SQLException e){throw new IllegalStateException("db read failed using Vault dynamic credentials",e);}}
}

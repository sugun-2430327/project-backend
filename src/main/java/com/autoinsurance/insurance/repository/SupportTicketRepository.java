package com.autoinsurance.insurance.repository;

import com.autoinsurance.insurance.model.SupportTicket;
import com.autoinsurance.insurance.model.TicketStatus;
import com.autoinsurance.insurance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    // Find all tickets by customer
    List<SupportTicket> findByUser(User user);
    
    // Find all tickets by customer ID
    List<SupportTicket> findByUserUserId(Long userId);
    
    // Find tickets by status
    List<SupportTicket> findByTicketStatus(TicketStatus ticketStatus);
    
    // Find tickets by customer and status
    List<SupportTicket> findByUserAndTicketStatus(User user, TicketStatus ticketStatus);
    
    // Find all open tickets
    @Query("SELECT t FROM SupportTicket t WHERE t.ticketStatus = 'OPEN' ORDER BY t.createdDate ASC")
    List<SupportTicket> findAllOpenTickets();
    
    // Find all resolved tickets
    @Query("SELECT t FROM SupportTicket t WHERE t.ticketStatus = 'RESOLVED' ORDER BY t.resolvedDate DESC")
    List<SupportTicket> findAllResolvedTickets();
    
    // Find tickets resolved by specific admin
    List<SupportTicket> findByResolvedBy(User resolvedBy);
    
    // Find all tickets ordered by creation date (most recent first)
    @Query("SELECT t FROM SupportTicket t ORDER BY t.createdDate DESC")
    List<SupportTicket> findAllOrderByCreatedDateDesc();
    
    // Find tickets by policy enrollment
    @Query("SELECT t FROM SupportTicket t WHERE t.policyEnrollment.enrollmentId = :enrollmentId")
    List<SupportTicket> findByPolicyEnrollmentId(@Param("enrollmentId") Long enrollmentId);
    
    // Find tickets by claim
    @Query("SELECT t FROM SupportTicket t WHERE t.claim.claimId = :claimId")
    List<SupportTicket> findByClaimId(@Param("claimId") Long claimId);
    
    // Count open tickets by customer
    @Query("SELECT COUNT(t) FROM SupportTicket t WHERE t.user = :user AND t.ticketStatus = 'OPEN'")
    long countOpenTicketsByUser(@Param("user") User user);
    
    // Count total tickets by customer
    long countByUser(User user);
}

use JobMDB
go

/*
    DATA SEED SCRIPT – Recruitment Management System
    Database: Microsoft SQL Server
    Author: NextGen Dev Team
    Created: 2025-11-06
    Description:
        Seed dữ liệu demo đầy đủ cho hệ thống tuyển dụng.
        Bao gồm: Account, Company, User, Skill, Candidate, Job Posting, Custom Form,
        Application, Interview, Evaluation Note, Offer, Question, TestAssignment,
        TestSubmission, RecruitmentStage, Report, AuditLog...
        Dành cho mục đích demo và kiểm thử (20–50 bản ghi/bảng, có quan hệ hợp lệ).
*/

-- ==============================================================
-- 1) ACCOUNT (tài khoản: ADMIN, HR, MANAGER, INTERVIEWER, CANDIDATE)
-- ==============================================================
INSERT INTO account ( email, password_hash, role, status)
VALUES
    ( 'admin01@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'ADMIN', 'ACTIVE'),

    ('hr01@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'HR', 'ACTIVE'),
    ('hr02@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'HR', 'ACTIVE'),
    ('hr03@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'HR', 'ACTIVE'),

    ('manager01@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'MANAGER', 'ACTIVE'),
    ('manager02@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'MANAGER', 'ACTIVE'),
    ('manager03@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'MANAGER', 'ACTIVE'),
    ('manager04@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'MANAGER', 'ACTIVE'),

    ('inter01@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter02@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter03@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter04@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter05@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter06@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE'),
    ('inter07@jms.com', '$2a$10$7ve.2zWHgzTWSHOUlkOCOuYTTxakAAkyrclvSpctNtixSkjkKbAGC', 'INTERVIEWER', 'ACTIVE');
GO

-- ==============================================================
-- 2) COMPANY (1 bản ghi)
-- ==============================================================
INSERT INTO company (name, address, website, phone, email, created_at, updated_at)
VALUES
(N'NextGen HR Solutions', N'123 Nguyễn Văn Cừ, Q.5, TP.HCM', N'https://nextgenhr.vn', '0281234567', 'contact@nextgenhr.vn', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 3) USER (nhân viên trong công ty, map tới account)
--    Bảng tên là [user] trong DB (không phải account)
-- ==============================================================
INSERT INTO [user] (full_name, phone, address, company_id, account_id)
VALUES
    (N'Nguyễn Văn Admin', '0900000001', N'Hà Nội', 1, 1),

    (N'Nguyễn Thị HR 01', '0900000002', N'Hà Nội', 1, 2),
    (N'Lê Văn HR 02', '0900000003', N'Đà Nẵng', 1, 3),
    (N'Trần HR 03', '0900000004', N'Hồ Chí Minh', 1, 4),

    (N'Phạm Manager 01', '0900000005', N'Hải Phòng', 1, 5),
    (N'Trần Manager 02', '0900000006', N'Nha Trang', 1, 6),
    (N'Lý Manager 03', '0900000007', N'Vinh', 1, 7),
    (N'Đỗ Manager 04', '0900000008', N'Huế', 1, 8),

    (N'Interview 01', '0900000009', N'Hà Nội', 1, 9),
    (N'Interview 02', '0900000010', N'Đà Nẵng', 1, 10),
    (N'Interview 03', '0900000011', N'Hồ Chí Minh', 1, 11),
    (N'Interview 04', '0900000012', N'Hà Nội', 1, 12),
    (N'Interview 05', '0900000013', N'Hải Phòng', 1, 13),
    (N'Interview 06', '0900000014', N'Huế', 1, 14),
    (N'Interview 07', '0900000015', N'Cần Thơ', 1, 15);

-- ==============================================================
-- 4) SKILL (danh mục kỹ năng)
-- ==============================================================
INSERT INTO skill (name, created_at, updated_at)
VALUES
(N'Java', GETDATE(), GETDATE()),
(N'Spring Boot', GETDATE(), GETDATE()),
(N'SQL', GETDATE(), GETDATE()),
(N'React', GETDATE(), GETDATE()),
(N'Testing', GETDATE(), GETDATE()),
(N'English', GETDATE(), GETDATE()),
(N'Communication', GETDATE(), GETDATE()),
(N'Teamwork', GETDATE(), GETDATE()),
(N'Leadership', GETDATE(), GETDATE()),
(N'Problem Solving', GETDATE(), GETDATE()),
(N'AWS', GETDATE(), GETDATE()),
(N'Python', GETDATE(), GETDATE()),
(N'Docker', GETDATE(), GETDATE()),
(N'HTML/CSS', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 5) CANDIDATE_PROFILE (khoảng 20 ứng viên)
-- ==============================================================
INSERT INTO candidate_profile (full_name, email, phone, career_goal, education, experience_years, previous_company, certificates, created_at, updated_at)
VALUES
(N'Nguyễn Văn A', 'candidate01@mail.com', '0901234001', N'Trở thành backend developer', N'Đại học Bách Khoa', 2, N'FPT Software', N'OCA, TOEIC 600', GETDATE(), GETDATE()),
(N'Lê Thị B', 'candidate02@mail.com', '0901234002', N'Chuyên gia QA', N'Đại học CNTT', 1, N'TMA Solutions', N'ISTQB Foundation', GETDATE(), GETDATE()),
(N'Phạm Minh C', 'candidate03@mail.com', '0901234003', N'Fullstack developer', N'Đại học KHTN', 3, N'VNG', N'OCP', GETDATE(), GETDATE()),
(N'Trần Văn D', 'candidate04@mail.com', '0901234004', N'Front-end engineer', N'Đại học FPT', 2, N'Startup X', N'Coursera React', GETDATE(), GETDATE()),
(N'Hoàng Thị E', 'candidate05@mail.com', '0901234005', N'QA Automation', N'Đại học CNTT', 4, N'TechOne', N'Selenium Cert', GETDATE(), GETDATE()),
(N'Bùi Văn F', 'candidate06@mail.com', '0901234006', N'Backend Java', N'Đại học Bách Khoa', 5, N'ABC Corp', N'OCP, AWS Certified', GETDATE(), GETDATE()),
(N'Võ Thị G', 'candidate07@mail.com', '0901234007', N'Data Engineer', N'Đại học Kinh tế', 3, N'DataCo', N'Training BigData', GETDATE(), GETDATE()),
(N'Ngô Văn H', 'candidate08@mail.com', '0901234008', N'DevOps', N'Đại học CNTT', 3, N'CloudVN', N'Docker Cert', GETDATE(), GETDATE()),
(N'Đinh Thị I', 'candidate09@mail.com', '0901234009', N'Junior QA', N'Đại học Sư phạm Kỹ thuật', 1, N'Intern', N'ISTQB', GETDATE(), GETDATE()),
(N'Phan Văn J', 'candidate10@mail.com', '0901234010', N'PHP Developer', N'Đại học Văn Lang', 2, N'WebStartup', N'HTML Cert', GETDATE(), GETDATE()),
(N'Trương Thị K', 'candidate11@mail.com', '0901234011', N'Fullstack Junior', N'Đại học CNTT', 1, N'Freelancer', N'React Course', GETDATE(), GETDATE()),
(N'Phùng Văn L', 'candidate12@mail.com', '0901234012', N'QA Manual', N'Đại học Công nghệ', 2, N'LocalCo', N'ISTQB', GETDATE(), GETDATE()),
(N'Nguyễn Văn M', 'candidate13@mail.com', '0901234013', N'Backend Java', N'Đại học Bách Khoa', 6, N'BigCorp', N'OCA, OCP', GETDATE(), GETDATE()),
(N'Hoàng Văn N', 'candidate14@mail.com', '0901234014', N'System Analyst', N'Đại học Khoa học', 4, N'Consulting', N'BA Cert', GETDATE(), GETDATE()),
(N'Lê Văn O', 'candidate15@mail.com', '0901234015', N'Front-end Junior', N'Đại học FPT', 1, N'Agency', N'CSS Cert', GETDATE(), GETDATE()),
(N'Phạm Thị P', 'candidate16@mail.com', '0901234016', N'QA Automation', N'Đại học CNTT', 3, N'AutomationCo', N'Selenium', GETDATE(), GETDATE()),
(N'Hồ Văn Q', 'candidate17@mail.com', '0901234017', N'Cloud Engineer', N'Đại học Bách Khoa', 5, N'CloudHub', N'AWS Cert', GETDATE(), GETDATE()),
(N'Vũ Thị R', 'candidate18@mail.com', '0901234018', N'Product Manager', N'Đại học Ngoại thương', 4, N'ProductCo', N'PMP', GETDATE(), GETDATE()),
(N'Đỗ Văn S', 'candidate19@mail.com', '0901234019', N'Backend Java', N'Đại học Bách Khoa', 2, N'SME', N'Java Cert', GETDATE(), GETDATE()),
(N'Kim Thị T', 'candidate20@mail.com', '0901234020', N'QA Fresh Graduate', N'Đại học Công nghệ', 0, N'Intern', N'', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 6) candidate_skill (liên kết many-to-many)
-- ==============================================================
-- Gán một số kỹ năng cho các candidate (tham chiếu bằng email + name)
INSERT INTO candidate_skill (candidate_id, skill_id)
VALUES
((SELECT id FROM candidate_profile WHERE email = 'candidate01@mail.com'), (SELECT id FROM skill WHERE name = N'Java')),
((SELECT id FROM candidate_profile WHERE email = 'candidate01@mail.com'), (SELECT id FROM skill WHERE name = N'Spring Boot')),
((SELECT id FROM candidate_profile WHERE email = 'candidate02@mail.com'), (SELECT id FROM skill WHERE name = N'Testing')),
((SELECT id FROM candidate_profile WHERE email = 'candidate02@mail.com'), (SELECT id FROM skill WHERE name = N'English')),
((SELECT id FROM candidate_profile WHERE email = 'candidate03@mail.com'), (SELECT id FROM skill WHERE name = N'React')),
((SELECT id FROM candidate_profile WHERE email = 'candidate03@mail.com'), (SELECT id FROM skill WHERE name = N'SQL')),
((SELECT id FROM candidate_profile WHERE email = 'candidate04@mail.com'), (SELECT id FROM skill WHERE name = N'React')),
((SELECT id FROM candidate_profile WHERE email = 'candidate05@mail.com'), (SELECT id FROM skill WHERE name = N'Testing')),
((SELECT id FROM candidate_profile WHERE email = 'candidate06@mail.com'), (SELECT id FROM skill WHERE name = N'Java')),
((SELECT id FROM candidate_profile WHERE email = 'candidate06@mail.com'), (SELECT id FROM skill WHERE name = N'AWS')),
((SELECT id FROM candidate_profile WHERE email = 'candidate07@mail.com'), (SELECT id FROM skill WHERE name = N'Python')),
((SELECT id FROM candidate_profile WHERE email = 'candidate08@mail.com'), (SELECT id FROM skill WHERE name = N'Docker')),
((SELECT id FROM candidate_profile WHERE email = 'candidate09@mail.com'), (SELECT id FROM skill WHERE name = N'Testing')),
((SELECT id FROM candidate_profile WHERE email = 'candidate10@mail.com'), (SELECT id FROM skill WHERE name = N'HTML/CSS')),
((SELECT id FROM candidate_profile WHERE email = 'candidate11@mail.com'), (SELECT id FROM skill WHERE name = N'React')),
((SELECT id FROM candidate_profile WHERE email = 'candidate12@mail.com'), (SELECT id FROM skill WHERE name = N'Testing')),
((SELECT id FROM candidate_profile WHERE email = 'candidate13@mail.com'), (SELECT id FROM skill WHERE name = N'Java')),
((SELECT id FROM candidate_profile WHERE email = 'candidate14@mail.com'), (SELECT id FROM skill WHERE name = N'Leadership')),
((SELECT id FROM candidate_profile WHERE email = 'candidate15@mail.com'), (SELECT id FROM skill WHERE name = N'HTML/CSS')),
((SELECT id FROM candidate_profile WHERE email = 'candidate16@mail.com'), (SELECT id FROM skill WHERE name = N'Testing'));
GO

-- ==============================================================
-- 7) DOCUMENT (CV / certificates)
-- ==============================================================
INSERT INTO document (candidate_id, document_name, file_path, file_type, uploaded_at, updated_at)
VALUES
((SELECT id FROM candidate_profile WHERE email = 'candidate01@mail.com'), N'CV Nguyễn Văn A', '/uploads/cv_nv_a.pdf', 'CV', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate02@mail.com'), N'CV Lê Thị B', '/uploads/cv_lt_b.pdf', 'CV', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate03@mail.com'), N'CV Phạm Minh C', '/uploads/cv_pm_c.pdf', 'CV', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate05@mail.com'), N'Selenium Cert', '/uploads/selenium_cert.pdf', 'CERTIFICATE', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate06@mail.com'), N'AWS Cert', '/uploads/aws_cert.pdf', 'CERTIFICATE', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate09@mail.com'), N'CV Đinh Thị I', '/uploads/cv_dt_i.pdf', 'CV', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email = 'candidate20@mail.com'), N'CV Kim Thị T', '/uploads/cv_kt_t.pdf', 'CV', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 8) JOB_POSTING (khoảng 8 tin tuyển)
-- ==============================================================
INSERT INTO job_posting (job_title, description, requirements, location, salary_range, status, company_id, created_at, updated_at)
VALUES
(N'Lập trình viên Java', N'Phát triển backend hệ thống enterprise', N'Tốt nghiệp CNTT, Java, Spring Boot, SQL', N'HCM', N'15-30 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'QA Engineer', N'Kiểm thử tự động và thủ công', N'ISTQB, Selenium, API testing', N'HCM', N'12-22 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'Front-end Developer', N'Phát triển ứng dụng React', N'React, HTML/CSS, JS', N'HCM', N'12-25 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'DevOps Engineer', N'CI/CD, Docker, Kubernetes', N'Docker, Kubernetes, Linux', N'HCM', N'18-35 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'Data Engineer', N'ETL, Big Data pipeline', N'Python, Spark, SQL', N'HCM', N'20-40 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'QA Automation Lead', N'Lãnh đạo team QA Automation', N'Testing, Selenium, Leadership', N'HCM', N'25-40 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'Junior QA', N'Kiểm thử thủ công cho dự án web', N'Muốn tuyển Fresh/Junior', N'HCM', N'8-12 triệu', 'OPEN',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE()),
(N'Fullstack Developer', N'Node.js + React', N'Node, React, SQL', N'HCM', N'16-30 triệu', 'PAUSED',
    (SELECT id FROM company WHERE name = N'NextGen HR Solutions'), GETDATE(), GETDATE());
GO

-- ==============================================================
-- 9) CUSTOM_FORM (form ứng tuyển cho job)
-- ==============================================================
INSERT INTO custom_form (form_name, description, form_structure_json, job_posting_id, is_active, created_at, updated_at)
VALUES
(N'Form ứng tuyển Java', N'Form thêm kỹ năng backend', N'[{"label":"Mô tả dự án","type":"text"},{"label":"Github","type":"text"}]',
    (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 1, GETDATE(), GETDATE()),
(N'Form ứng tuyển QA', N'Form QA yêu cầu kỹ năng tự động hóa', N'[{"label":"Kinh nghiệm automation","type":"text"},{"label":"Tools","type":"checkbox","options":["Selenium","Appium"]}]',
    (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 1, GETDATE(), GETDATE()),
(N'Form FE', N'Form front-end', N'[{"label":"Portfolio","type":"text"},{"label":"Link demo","type":"text"}]',
    (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 1, GETDATE(), GETDATE()),
(N'Form DevOps', N'Form DevOps', N'[{"label":"Tools","type":"text"},{"label":"Kinh nghiệm Cloud","type":"text"}]',
    (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 1, GETDATE(), GETDATE()),
(N'Form Junior QA', N'Form cho vị trí Junior QA', N'[{"label":"Sinh viên tốt nghiệp","type":"radio","options":["Yes","No"]}]',
    (SELECT id FROM job_posting WHERE job_title = N'Junior QA'), 1, GETDATE(), GETDATE());
GO

-- ==============================================================
-- 10) APPLICATION (khoảng 30 đơn ứng tuyển)
--    (Tham chiếu candidate_profile và job_posting bằng email/job_title)
-- ==============================================================
INSERT INTO application (candidate_id, job_posting_id, status, note, source, created_at, updated_at)
VALUES
((SELECT id FROM candidate_profile WHERE email='candidate01@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 'APPLIED', N'Ứng tuyển trực tiếp', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate02@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'INTERVIEWING', N'Pass CV', N'LinkedIn', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate03@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 'APPLIED', N'Portfolio tốt', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate04@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 'APPLIED', N'LinkedIn', N'LinkedIn', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate05@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'INTERVIEWING', N'Automation background', N'Referral', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate06@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 'APPLIED', N'Cloud skills', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate07@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Data Engineer'), 'APPLIED', N'Data projects', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate08@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 'APPLIED', N'Docker experience', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate09@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Junior QA'), 'APPLIED', N'Fresh graduate', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate10@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 'REJECTED', N'Lack experience', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate11@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 'APPLIED', N'Good portfolio', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate12@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'INTERVIEWING', N'Good manual skills', N'LinkedIn', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate13@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 'APPLIED', N'Experienced', N'Referral', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate14@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Automation Lead'), 'APPLIED', N'Leadership skill', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate15@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Front-end Developer'), 'APPLIED', N'Junior', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate16@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'APPLIED', N'Automation experience', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate17@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 'APPLIED', N'Cloud', N'LinkedIn', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate18@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Fullstack Developer'), 'INTERVIEWING', N'Applying for fullstack', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate19@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 'INTERVIEWING', N'Good backend skill', N'Referral', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate20@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Junior QA'), 'APPLIED', N'Fresh graduate', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate01@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Fullstack Developer'), 'APPLIED', N'Also interested in fullstack', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate05@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Automation Lead'), 'APPLIED', N'Apply for lead', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate06@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 'INTERVIEWING', N'Interview scheduled', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate07@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Data Engineer'), 'INTERVIEWING', N'Shortlisted', N'LinkedIn', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate08@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'DevOps Engineer'), 'REJECTED', N'Not fit', N'Email', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate13@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 'OFFERED', N'Strong candidate', N'Referral', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate19@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'Lập trình viên Java'), 'INTERVIEWING', N'On-site interview', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate02@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'OFFERED', N'Offer prepared', N'Website', GETDATE(), GETDATE()),
((SELECT id FROM candidate_profile WHERE email='candidate05@mail.com'), (SELECT id FROM job_posting WHERE job_title = N'QA Engineer'), 'REJECTED', N'Failed technical', N'Email', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 11) INTERVIEW (khoảng 12 cuộc)
--    Tham chiếu application và interviewer (user)
-- ==============================================================
INSERT INTO interview (application_id, interviewer_id, interview_date, location, result, notes, created_at, updated_at)
VALUES
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate02@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'QA Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, -3, GETDATE()), N'Online - Google Meet', N'Pass', N'Ứng viên nắm tốt automation', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate05@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'QA Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, -10, GETDATE()), N'Office - Phòng A', N'Fail', N'Thi coding test chưa đạt', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate06@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'DevOps Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, 1, GETDATE()), N'Online - Zoom', N'Pending', N'Chưa có kết quả', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate07@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Data Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, -2, GETDATE()), N'Office - Phòng B', N'Pending', N'Cần đánh giá thêm', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate13@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Lập trình viên Java')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, -1, GETDATE()), N'Office - Phòng C', N'Pass', N'Ứng viên có kinh nghiệm', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate19@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Lập trình viên Java')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, 2, GETDATE()), N'Online - Teams', N'Pending', N'Chưa phỏng vấn', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate11@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Front-end Developer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, -5, GETDATE()), N'Office - Phòng A', N'Pass', N'UI tốt, JS tốt', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate12@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'QA Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, -4, GETDATE()), N'Online - Google Meet', N'Pass', N'Ứng viên manual tốt', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate02@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'QA Engineer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, -1, GETDATE()), N'Office - Phòng A', N'Pass', N'Ứng viên có tiềm năng', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate03@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Front-end Developer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, 0, GETDATE()), N'Online - Zoom', N'Pending', N'Đang chờ kết quả', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate20@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Junior QA')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter02@jms.com')),
    DATEADD(day, -2, GETDATE()), N'Office - Phòng B', N'Pass', N'Ứng viên fresh tốt', GETDATE(), GETDATE()),
((SELECT TOP 1 id FROM application WHERE candidate_id = (SELECT TOP 1 id FROM candidate_profile WHERE email='candidate01@mail.com') AND job_posting_id = (SELECT TOP 1 id FROM job_posting WHERE job_title = N'Fullstack Developer')),
    (SELECT TOP 1 id FROM [user] WHERE account_id = (SELECT TOP 1 id FROM account WHERE email='inter01@jms.com')),
    DATEADD(day, 3, GETDATE()), N'Online - Teams', N'Pending', N'Ứng viên mong muốn fullstack', GETDATE(), GETDATE());
GO



-- ============================================================== 
-- 12) EVALUATION_NOTE (ghi chú đánh giá) - email version
-- ==============================================================

INSERT INTO evaluation_note (interview_id, evaluator_id, content, rating, created_at, updated_at)
VALUES
(2, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter01@jms.com')), N'Ứng viên có kỹ năng automation tốt, recommend vào vòng tiếp theo', 5, GETDATE(), GETDATE()),
(3, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter02@jms.com')), N'Chưa đáp ứng yêu cầu coding', 2, GETDATE(), GETDATE()),
(6, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter01@jms.com')), N'Ứng viên có nhiều dự án thực tế', 5, GETDATE(), GETDATE()),
(8, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter03@jms.com')), N'Front-end giỏi về UI', 4, GETDATE(), GETDATE()),
(9, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter03@jms.com')), N'Ứng viên manual test tốt', 4, GETDATE(), GETDATE()),
(12, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter01@jms.com')), N'Candidate fresh, nhiệt huyết', 3, GETDATE(), GETDATE()),
(10, (SELECT id FROM [user] WHERE account_id = (SELECT id FROM account WHERE email='inter02@jms.com')), N'Đang chờ feedback từ team', NULL, GETDATE(), GETDATE());
GO


-- ==============================================================
-- 13) OFFER (một vài offer)
-- ==============================================================
INSERT INTO offer (application_id, position_title, salary_offer, start_date, status, created_at, updated_at)
VALUES
(9, N'Lập trình viên Java', 30000.0, DATEADD(day, 30, GETDATE()), N'ACCEPTED', GETDATE(), GETDATE()),
(11, N'QA Engineer', 18000.0, DATEADD(day, 21, GETDATE()), N'PENDING', GETDATE(), GETDATE()),
(12, N'Lập trình viên Java', 22000.0, DATEADD(day, 45, GETDATE()), N'PENDING', GETDATE(), GETDATE()),
(18, N'QA Automation Lead', 30000.0, DATEADD(day, 60, GETDATE()), N'REJECTED', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 14) QUESTION (câu hỏi test)
-- ==============================================================
INSERT INTO question (question_text, correct_answer, question_type, created_at, updated_at)
VALUES
(N'Explain SOLID principles in OOP', NULL, N'ESSAY', GETDATE(), GETDATE()),
(N'What is the output of: System.out.println(1+2+\"3\")?', N'33', N'MULTIPLE_CHOICE', GETDATE(), GETDATE()),
(N'Write a SQL query to find duplicate rows', NULL, N'CODING', GETDATE(), GETDATE()),
(N'What is REST API?', NULL, N'TEXT', GETDATE(), GETDATE()),
(N'How do you handle transaction in Spring?', NULL, N'ESSAY', GETDATE(), GETDATE()),
(N'What is a closure in JavaScript?', NULL, N'TEXT', GETDATE(), GETDATE()),
(N'Explain the difference between PUT and PATCH', NULL, N'TEXT', GETDATE(), GETDATE()),
(N'What command builds a Docker image?', N'docker build .', N'TEXT', GETDATE(), GETDATE()),
(N'How to improve SQL query performance?', NULL, N'ESSAY', GETDATE(), GETDATE()),
(N'Write function to reverse a string', NULL, N'CODING', GETDATE(), GETDATE()),
(N'What is CI/CD?', NULL, N'TEXT', GETDATE(), GETDATE()),
(N'Explain indexing in databases', NULL, N'ESSAY', GETDATE(), GETDATE()),
(N'How would you test an API endpoint?', NULL, N'ESSAY', GETDATE(), GETDATE()),
(N'Describe event loop in Node.js', NULL, N'TEXT', GETDATE(), GETDATE()),
(N'What is polymorphism?', NULL, N'TEXT', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 15) TEST_ASSIGNMENT (giao bài test cho ứng viên)
-- ==============================================================
INSERT INTO test_assignment (test_title, description, deadline, created_at, updated_at)
VALUES
(N'Java Backend Test', N'Task: REST API + DB', DATEADD(day, 7, GETDATE()), GETDATE(), GETDATE()),
(N'Front-end Coding Test', N'Build a small React app', DATEADD(day, 5, GETDATE()), GETDATE(), GETDATE()),
(N'QA Automation Test', N'Selenium basic tasks', DATEADD(day, 6, GETDATE()), GETDATE(), GETDATE()),
(N'DevOps Task', N'CI/CD pipeline setup', DATEADD(day, 10, GETDATE()), GETDATE(), GETDATE()),
(N'Data Engineering Test', N'ETL task', DATEADD(day, 8, GETDATE()), GETDATE(), GETDATE()),
(N'Junior QA Test', N'Manual test cases', DATEADD(day, 4, GETDATE()), GETDATE(), GETDATE());
GO

-- ==============================================================
-- 16) assessment_question (bảng nối test_assignment <-> question)
-- ==============================================================
-- Gán một số câu hỏi cho từng test
INSERT INTO assessment_question (assessment_id, question_id)
VALUES
((SELECT id FROM test_assignment WHERE test_title = N'Java Backend Test'), 1),
((SELECT id FROM test_assignment WHERE test_title = N'Java Backend Test'), 3),
((SELECT id FROM test_assignment WHERE test_title = N'Front-end Coding Test'), 10),
((SELECT id FROM test_assignment WHERE test_title = N'QA Automation Test'), 5),
((SELECT id FROM test_assignment WHERE test_title = N'DevOps Task'), 8),
((SELECT id FROM test_assignment WHERE test_title = N'Junior QA Test'), 13);
GO

-- ==============================================================
-- 17) TEST_SUBMISSION (bài làm của ứng viên)
-- ==============================================================
INSERT INTO test_submission (test_assignment_id, submitted_at, score, answers, created_at, updated_at)
VALUES
((SELECT id FROM test_assignment WHERE test_title = N'Java Backend Test'),
    DATEADD(day, 3, GETDATE()), 85.0, N'{"q1":"SOLID...","q2":"SELECT ..."}', GETDATE(), GETDATE()),
((SELECT id FROM test_assignment WHERE test_title = N'Front-end Coding Test'),
    DATEADD(day, 2, GETDATE()), 78.0, N'{"app":"deployed","notes":"..."}', GETDATE(), GETDATE()),
((SELECT id FROM test_assignment WHERE test_title = N'QA Automation Test'),
    DATEADD(day, 4, GETDATE()), 90.0, N'{"cases":"..."}', GETDATE(), GETDATE()),
((SELECT id FROM test_assignment WHERE test_title = N'Junior QA Test'),
    DATEADD(day, 1, GETDATE()), 70.0, N'{"cases":"..."}', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 18) RECRUITMENT_STAGE (các giai đoạn tuyển dụng)
-- ==============================================================
INSERT INTO recruitment_stage (stage_name, description, created_at, updated_at)
VALUES
(N'Sàng lọc hồ sơ', N'Kiểm tra CV và thông tin ban đầu', GETDATE(), GETDATE()),
(N'Phỏng vấn kỹ thuật', N'Đánh giá chuyên môn', GETDATE(), GETDATE()),
(N'Đề nghị Offer', N'Thỏa thuận lương và điều khoản', GETDATE(), GETDATE()),
(N'Onboarding', N'Chuẩn bị hồ sơ và ngày bắt đầu', GETDATE(), GETDATE()),
(N'Closed', N'Vị trí đã tuyển xong', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 19) REPORT (một vài báo cáo demo)
-- ==============================================================
INSERT INTO report (report_type, data, time_range, created_at, updated_at)
VALUES
(N'Recruitment Funnel', N'{"applied":120,"interviewed":40,"offered":10,"hired":5}', N'2025-10', GETDATE(), GETDATE()),
(N'KPI by Department', N'{"HR":{"time_to_hire":25},"ENG":{"time_to_hire":30}}', N'2025-Q3', GETDATE(), GETDATE()),
(N'Source Analysis', N'{"jobboard":60,"referral":30,"linkedin":30}', N'2025-01-10/2025-11-05', GETDATE(), GETDATE());
GO

-- ==============================================================
-- 20) AUDIT_LOG (khoảng 40 bản ghi ghi lại hành động)
--    audit_log.user -> tham chiếu bảng [user]
-- ==============================================================

-- ==============================================================
-- End of seed script
-- ==============================================================

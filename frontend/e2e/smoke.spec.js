import { test, expect } from '@playwright/test';

test('landing page exposes the primary candidate journey', async ({ page }) => {
  await page.goto('/');
  await expect(page.getByRole('heading', { name: 'Hiring should feel like progress.' })).toBeVisible();
  await page.getByRole('link', { name: 'Explore openings' }).click();
  await expect(page).toHaveURL(/\/jobs$/);
  await expect(page.getByRole('heading', { name: 'Find your next good move.' })).toBeVisible();
});

test('registration form supports candidate and recruiter roles', async ({ page }) => {
  await page.goto('/register');
  await expect(page.getByRole('heading', { name: 'Start your TalentSync journey' })).toBeVisible();
  await page.getByLabel('Account type').selectOption('RECRUITER');
  await expect(page.getByLabel('Organization')).toBeVisible();
  await page.getByLabel('Account type').selectOption('CANDIDATE');
  await expect(page.getByLabel('Organization')).toBeHidden();
});

test('candidate can log in and reach the dashboard', async ({ page }) => {
  await page.goto('/login');
  await page.getByLabel('Email').fill('candidate@example.com');
  await page.getByLabel('Password').fill('Password123');
  await page.getByRole('button', { name: 'Sign in' }).click();
  await expect(page).toHaveURL(/\/dashboard$/);
  await expect(page.getByRole('heading', { name: 'Hello, Alex.' })).toBeVisible();
});

test('jobs page loads default professional job listings from the API', async ({ page }) => {
  await page.goto('/jobs');
  await expect(page.getByRole('heading', { name: 'Find your next good move.' })).toBeVisible();
  await expect(page.locator('.notice')).toHaveCount(0);
  await expect(page.locator('.job')).toHaveCount(4);
  await expect(page.getByText('Senior Java Cloud Architect')).toBeVisible();
});

export interface User {
    id: number;
    username: string,
    token: null,
    created: Date,
    lastLogin: string,
    loginAttempts: number,
    lastLoginAttempt: string | null,
    role: string,
    locked: boolean
}
